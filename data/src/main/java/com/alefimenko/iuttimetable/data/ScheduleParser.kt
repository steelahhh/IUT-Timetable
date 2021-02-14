package com.alefimenko.iuttimetable.data

import android.annotation.SuppressLint
import com.alefimenko.iuttimetable.data.remote.model.ClassEntry
import com.alefimenko.iuttimetable.data.remote.model.Time
import com.alefimenko.iuttimetable.data.remote.model.WeekSchedule
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/*
 * Created by Alexander Efimenko on 2019-01-16.
 */

@SuppressLint("DefaultLocale")
class ScheduleParser {
    private var _doc: Document? = null

    val document: Document
        get() = _doc ?: error("Document should not be null")

    val weeks: List<String>
        get() = document.weeks

    val weeksCount: Int
        get() = document.weeks.size

    val semester: String
        get() = document.semester

    fun initialize(body: String) {
        _doc = Jsoup.parse(body)
    }

    val schedule: Map<Int, WeekSchedule>
        get() = with(document) {
            val weeksNumber = weeks.size
            val schedule = mutableMapOf<Int, WeekSchedule>()
            for (week in 0 until weeksNumber) {
                schedule[week] = mutableListOf()
                for (day in 2..7) {
                    schedule[week]?.add(getDaySchedule(week, day))
                }
            }
            return schedule
        }

    private fun Document.getDaySchedule(week: Int, day: Int): List<ClassEntry> =
        getWeekRow(week).fold(mutableListOf()) { acc, element ->
            val entry = element.child(day).parsed.takeIf {
                it.isNotEmpty()
            } ?: ""
            val time = element.child(1).parsed.split("-")
            if (entry.isNotEmpty()) {
                if (!entry.hasMultipleClasses) {
                    acc.add(
                        ClassEntry(
                            subject = entry.subjectTeacher.first,
                            teacher = entry.subjectTeacher.second,
                            classType = entry.classType,
                            time = Time(start = time[0], finish = time[1]),
                            location = entry.location,
                            innerGroup = 0.innerGroup,
                            date = document.getDate(week, day)
                        )
                    )
                } else {
                    val cells = entry.split("\\) ".toRegex())
                    cells.forEachIndexed { idx, s ->
                        val cell = when {
                            idx != cells.size - 1 -> "$s)"
                            else -> s
                        }
                        acc.add(
                            ClassEntry(
                                subject = cell.subjectTeacher.first,
                                teacher = cell.subjectTeacher.second,
                                classType = cell.classType,
                                time = Time(start = time[0], finish = time[1]),
                                location = cell.location,
                                innerGroup = (idx + 1).innerGroup,
                                date = document.getDate(week, day)
                            )
                        )
                    }
                }
            }
            return@fold acc
        }

    /**
     * change the rows content
     *
     * @param week id to switch to
     */
    private fun Document.getWeekRow(week: Int): Elements =
        select("table.time_table")[week].select("> tbody > tr")

    /**
     * @return list of week titles
     */
    private val Document.weeks: List<String>
        get() = select("caption").text().split(" ").map { title ->
            val firstLetter = title.substring(0, 1)
            if (firstLetter.matches("[0-9]".toRegex())) {
                "$title неделя"
            } else {
                title.capitalize()
            }
        }

    /**
     * @return current semester string
     */
    val Document.semester: String
        get() {
            val table = select("table").first().select("> tbody > tr").text().trim()
            return when {
                table.contains(" на ") -> {
                    table.substring(
                        table.indexOf(" на ") + 3,
                        table.indexOf(" уч") + 1
                    ).trim()
                }
                else -> {
                    val sem = table.substring(0, table.indexOf(" учеб")).split(" ")
                    sem.takeLast(3).joinToString(separator = " ")
                }
            }
        }

    private fun Document.getDate(week: Int, day: Int): String {
        val date = select("thead")[week].select("> tr")[0].child(day).text()
        return if (date.toLowerCase().matches("[а-я]+ [0-9]{2}.[0-9]{2}(.[0-9]+)?".toRegex())) {
            date.split(" ")[1]
        } else {
            EMPTY_ENTRY
        }
    }

    private val String.hasMultipleClasses: Boolean
        get() = matches(".+([0-9]\\)|[0-9]\\)\\)) [а-яА-Я].+".toRegex())

    private val Element.parsed: String
        get() = text().trim().replace(
            AMBIGUOUS_SPACE,
            SPACE
        )

    private val String.location: String
        get() {
            val loc = substring(indexOf("),") + 2).trim()
            return "${loc.substring(0, loc.indexOf('('))} ${loc.substring(loc.indexOf('('))}"
        }

    private val String.classType: ClassType
        get() = when {
            contains("(пр)") -> ClassType.Practicum
            contains("(л)") -> ClassType.Lecture
            contains("(лаб)") -> ClassType.LabWork
            contains("(зач)") -> ClassType.FakeExam
            contains("(экз)") -> ClassType.Exam
            contains("(ул)") -> ClassType.Outside
            else -> ClassType.Unknown
        }

    private val String.subjectTeacher: Pair<String, String>
        get() {
            // Matches [Surname F. P.] or [Surname F.]
            val teacherFullNamePattern = Regex("[а-яА-Я]* [А-Яа-я]\\.( )?[А-Яа-я]?\\.?")

            val nameMatches = teacherFullNamePattern.findAll(this)
                .map { it.value.trim() }
                .filter { it.length > 3 }

            val teacher = nameMatches.firstOrNull()
                ?.takeIf { !hasVacancy } ?: VACANCY

            val idx = try {
                toLowerCase().indexOf(teacher.toLowerCase())
            } catch (expected: Exception) {
                this.length
            }

            val subject = if (idx != -1)
                substring(0, idx)
            else
                EMPTY_ENTRY

            return subject.capitalize() to teacher.capitalize()
        }

    private val String.hasVacancy: Boolean
        get() = toLowerCase().contains(VACANCY.toRegex())

    private val Int.innerGroup: String
        get() = if (this != 0) {
            "$this-я ПОДГРУППА"
        } else {
            EMPTY_ENTRY
        }

    companion object {
        const val EMPTY_ENTRY = "0"
        const val SPACE = ' '
        const val AMBIGUOUS_SPACE = 160.toChar()
        const val VACANCY = "вакансия"
    }
}
