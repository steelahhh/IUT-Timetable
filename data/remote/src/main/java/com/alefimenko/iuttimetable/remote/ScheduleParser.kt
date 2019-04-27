package com.alefimenko.iuttimetable.remote

import com.alefimenko.iuttimetable.remote.model.ClassEntry
import com.alefimenko.iuttimetable.remote.model.Time
import com.alefimenko.iuttimetable.remote.model.WeekSchedule
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.Locale

/*
 * Created by Alexander Efimenko on 2019-01-16.
 */

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
            } ?: return@fold mutableListOf<ClassEntry>()
            val time = element.child(1).parsed.split("-")
            if (entry.isNotEmpty()) {
                if (!entry.hasMultipleClasses) {
                    acc.add(
                        ClassEntry(
                            subject = entry.classInfo.first,
                            teacher = entry.classInfo.second,
                            classType = entry.classType,
                            time = Time(
                                start = time[0],
                                finish = time[1]
                            ),
                            location = entry.location,
                            innerGroup = 0.innerGroup,
                            date = document.getDate(week, day)
                        )
                    )
                } else {
                    val cells = entry.split("\\) ".toRegex())
                    cells.mapIndexed { idx, s ->
                        val cell = when {
                            idx != cells.size - 1 -> "$s)"
                            else -> s
                        }
                        acc.add(
                            ClassEntry(
                                subject = cell.classInfo.first,
                                teacher = cell.classInfo.second,
                                classType = cell.classType,
                                time = Time(
                                    start = time[0],
                                    finish = time[1]
                                ),
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
                "${firstLetter.toUpperCase()}${title.substring(1)}"
            }
        }

    /**
     * @return current semester string
     */
    val Document.semester: String
        get() {
            val table = select("table").first().select("> tbody > tr").text().trim()
            return when {
                table.contains(" на ") -> table.substring(
                    table.indexOf(" на ") + 3,
                    table.indexOf(" уч") + 1
                ).trim()
                else -> {
                    val sem = table.substring(0, table.indexOf(" учеб")).split(" ")
                    sem[sem.size - 3] + " " + sem[sem.size - 2] + " " + sem[sem.size - 1]
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

    private val String.classType: String
        get() = when {
            contains("(пр)".toRegex()) -> "прак"
            contains("(л)".toRegex()) -> "лек"
            contains("(лаб)".toRegex()) -> "лаб"
            contains("(зач)".toRegex()) -> "зач"
            contains("(экз)".toRegex()) -> "экз"
            contains("(ул)".toRegex()) -> "ул"
            else -> "---"
        }

    private val String.subjectAndTeacher: List<String>
        get() = substring(0, indexOf('(')).trim().split(" ".toRegex())

    private val String.classInfo: Pair<String, String>
        get() {
            var subject = StringBuilder()
            val subjNTeach = subjectAndTeacher
            val until = if (hasVacancy) 1 else 2
            if (subjNTeach.size > 2) {
                for (i in 0 until subjNTeach.size - until) {
                    subject.append(subjNTeach[i]).append(" ")
                }
            } else {
                subject = StringBuilder(subjNTeach[0])
            }

            val teacher = if (subjNTeach.size > 2 && !hasVacancy) {
                // subjNTeach has a full name of a teacher
                subjNTeach[subjNTeach.size - 2] + " " + subjNTeach[subjNTeach.size - 1]
            } else {
                // subjNTeach only contains 2 elements (subject - teacher)
                subjNTeach[subjNTeach.size - 1]
            }

            return subject.toString().toUpperCase(Locale.US) to teacher
        }

    private val String.hasVacancy: Boolean
        get() {
            return contains("вакансия".toRegex())
        }

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
    }
}
