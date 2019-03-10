package com.alefimenko.iuttimetable

import com.alefimenko.iuttimetable.core.data.ScheduleParser
import org.jsoup.Jsoup
import org.junit.Before
import org.junit.Test

/*
 * Created by Alexander Efimenko on 2019-01-20.
 */

class ParserTest {

    private val scheduleParser = ScheduleParser()

    @Before
    fun init() {
        scheduleParser.initialize(Jsoup.parse(HTML))
    }

    @Test
    fun `Should be two weeks`() {
        assert(scheduleParser.getSchedule().keys.size == 2)
    }

    @Test
    fun `Should contain spring`() {
        assert(scheduleParser.semester == "весенний семестр 2018-2019")
    }

    @Test
    fun `Should not be empty`() {
        assert(scheduleParser.getSchedule()[0]?.isNotEmpty() == true)
        assert(scheduleParser.getSchedule()[1]?.isNotEmpty() == true)
    }

    @Test
    fun `Should contain 6 days`() {
        assert(scheduleParser.getSchedule()[0]?.size == 6)
        assert(scheduleParser.getSchedule()[1]?.size == 6)
    }
}

private val HTML = """
<!-- saved from url=(0074)https://www.tyuiu.ru/shedule_new/bin/groups.py?act=show&print=&sgroup=5307 -->
<html><head><meta http-equiv="Content-Type" content="text/html; charset=KOI8-R">
        <title>Расписание</title>
        <link rel="stylesheet" type="text/css" href="./Расписание_files/view.css">
</head>

<body>

	<table>
		<colgroup><col width="250px"><col width="470px"><col width="250px">
		</colgroup><tbody><tr>
			<td>&nbsp;</td>
			<td style="text-align:center; font-size:12pt;">Расписание занятий группы А б-18-1
				<br><span style="font-size:12pt;">весенний семестр 2018-2019 учебного года</span></td>
			<td style="text-align:right;">
				<span></span><br>
				<span></span><br>
				<span></span><br>
			</td>
		</tr>
	</tbody></table>







<table class="time_table">
<colgroup><col width="8px"><col width="32px"><col width="155px"><col width="155px"><col width="155px"><col width="155px"><col width="155px"><col width="155px">
</colgroup><caption>нечетная</caption>
<thead>
	<tr><td>&nbsp;</td>
	<td>время</td>

		<td>понедельник</td>


		<td>вторник</td>


		<td>среда</td>


		<td>четверг</td>


		<td>пятница</td>


		<td>суббота</td>

</tr></thead>
<tbody>

<tr>
	<td>1</td>
	<td>08:00 - 09:35</td>

		<td>&nbsp;</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Рисунок  Зыков К.Н.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">Рисунок  вакансия (пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td>&nbsp;</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">живопись и колористика Зыков К.Н.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">живопись и колористика Козловская О.Л.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td>&nbsp;</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Начертательная геометрия Крамаровская В.И.(пр), 410(Корпус 8/1 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">Начертательная геометрия Романова А.А.(пр), 410(Корпус 8/1 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>

</tr>


<tr>
	<td>2</td>
	<td>09:45 - 11:20</td>

		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">архитектурная графика Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">архитектурная графика Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Рисунок  Зыков К.Н.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">Рисунок  вакансия (пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">История архитектуры Панфилов А.В.(л), 452(Корпус 8 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">живопись и колористика Зыков К.Н.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">живопись и колористика Козловская О.Л.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">математика Фомина В.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Геодезия Архипова Т.Д.(пр), 342(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>

</tr>


<tr>
	<td>3</td>
	<td>11:30 - 13:05</td>

		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">архитектурная графика Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">архитектурная графика Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">иностранный язык Ситева С.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Начертательная геометрия Крамаровская В.И.(л), 913(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">основы архитектурного проектирования Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">основы архитектурного проектирования Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">математика Фомина В.В.(л), 214(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Геодезия Архипова Т.Д.(л), 604(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>

</tr>


<tr>
	<td>4</td>
	<td>13:45 - 15:20</td>

		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">основы архитектурного проектирования Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">основы архитектурного проектирования Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">иностранный язык Ситева С.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Элективные курсы по физической культуре вакансия (пр), с/з(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">основы архитектурного проектирования Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">основы архитектурного проектирования Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Элективные курсы по физической культуре вакансия (пр), с/з(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td>&nbsp;</td>

</tr>


<tr>
	<td>5</td>
	<td>15:30 - 17:05</td>

		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">основы архитектурного проектирования Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">основы архитектурного проектирования Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td>&nbsp;</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">композиционное моделирование Мальцева Е.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">композиционное моделирование Кукоарэ Д.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td>&nbsp;</td>


		<td>&nbsp;</td>


		<td>&nbsp;</td>

</tr>

</tbody>
</table>


<table class="time_table">
<colgroup><col width="8px"><col width="32px"><col width="155px"><col width="155px"><col width="155px"><col width="155px"><col width="155px"><col width="155px">
</colgroup><caption>четная</caption>
<thead>
	<tr><td>&nbsp;</td>
	<td>время</td>

		<td>понедельник</td>


		<td>вторник</td>


		<td>среда</td>


		<td>четверг</td>


		<td>пятница</td>


		<td>суббота</td>

</tr></thead>
<tbody>

<tr>
	<td>1</td>
	<td>08:00 - 09:35</td>

		<td>&nbsp;</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Рисунок  Зыков К.Н.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">Рисунок  вакансия (пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td>&nbsp;</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">живопись и колористика Зыков К.Н.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">живопись и колористика Козловская О.Л.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td>&nbsp;</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Начертательная геометрия Крамаровская В.И.(пр), 410(Корпус 8/1 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">Начертательная геометрия Романова А.А.(пр), 410(Корпус 8/1 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>

</tr>


<tr>
	<td>2</td>
	<td>09:45 - 11:20</td>

		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">архитектурная графика Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">архитектурная графика Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Рисунок  Зыков К.Н.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">Рисунок  вакансия (пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">История архитектуры Панфилов А.В.(л), 452(Корпус 8 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">живопись и колористика Зыков К.Н.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">живопись и колористика Козловская О.Л.(пр), 334(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td>&nbsp;</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Геодезия Архипова Т.Д.(пр), 342(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>

</tr>


<tr>
	<td>3</td>
	<td>11:30 - 13:05</td>

		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">архитектурная графика Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">архитектурная графика Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">иностранный язык Ситева С.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Начертательная геометрия Крамаровская В.И.(л), 913(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">основы архитектурного проектирования Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">основы архитектурного проектирования Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td>&nbsp;</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Геодезия Архипова Т.Д.(л), 604(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>

</tr>


<tr>
	<td>4</td>
	<td>13:45 - 15:20</td>

		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">основы архитектурного проектирования Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">основы архитектурного проектирования Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td>&nbsp;</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Элективные курсы по физической культуре вакансия (пр), с/з(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">основы архитектурного проектирования Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">основы архитектурного проектирования Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Элективные курсы по физической культуре вакансия (пр), с/з(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>


		<td>&nbsp;</td>

</tr>


<tr>
	<td>5</td>
	<td>15:30 - 17:05</td>

		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">основы архитектурного проектирования Курмаз Ю.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">основы архитектурного проектирования Черенева (пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td>&nbsp;</td>


		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">композиционное моделирование Мальцева Е.В.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%">композиционное моделирование Кукоарэ Д.(пр), 301(Корпус 9 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>


		<td>&nbsp;</td>


		<td>&nbsp;</td>


		<td>&nbsp;</td>

</tr>

</tbody>
</table>



<p>&nbsp;</p>

<script src="./Расписание_files/jquery.min.js"></script>
<script type="text/javascript">
    ${'$'}(document).ready(function(){
        ${'$'}("a.button_close_").click(function(){
            window.close();
        })
    });
</script>



</body></html>
""".trimIndent()
