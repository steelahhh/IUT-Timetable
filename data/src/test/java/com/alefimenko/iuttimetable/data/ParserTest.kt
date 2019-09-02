package com.alefimenko.iuttimetable.data

import org.junit.Before
import org.junit.Test

/*
 * Created by Alexander Efimenko on 2019-01-20.
 */

class ParserTest {

    private val scheduleParser = ScheduleParser()

    @Before
    fun init() {
        scheduleParser.initialize(HTML)
    }

    @Test
    fun `Should be two weeks`() {
        assert(scheduleParser.schedule.keys.size == 2)
    }

    @Test
    fun `Should contain semester`() {
        assert(scheduleParser.semester.contains("семестр \\d*-\\d*".toRegex()))
    }

    @Test
    fun `Should not be empty`() {
        assert(scheduleParser.schedule[0]?.isNotEmpty() == true)
        assert(scheduleParser.schedule[1]?.isNotEmpty() == true)
    }

    @Test
    fun `Should contain 6 days`() {
        assert(scheduleParser.schedule[0]?.size == 6)
        assert(scheduleParser.schedule[1]?.size == 6)
    }

    private val HTML = """
        
<!-- saved from url=(0074)https://www.tyuiu.ru/shedule_new/bin/groups.py?act=show&print=&sgroup=4819 -->
<html><head><meta http-equiv="Content-Type" content="text/html; charset=KOI8-R">
        <title>Расписание</title>
        <link rel="stylesheet" type="text/css" href="./Расписание_files/view.css">
</head>

<body>

	<table>
		<colgroup><col width="250px"><col width="470px"><col width="250px">
		</colgroup><tbody><tr>
			<td>&nbsp;</td>
			<td style="text-align:center; font-size:12pt;">Расписание занятий группы ИСТ б-16-1
				<br><span style="font-size:12pt;">осенний семестр 2019-2020 учебного года</span></td>
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
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Планирование эксперимента и обработка эксперементальных данных Николаева Д.Р.(лаб), 612(Корпус 8/1 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%"> </td>

</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
</tr>


<tr>
	<td>2</td>
	<td>09:45 - 11:20</td>
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Планирование эксперимента и обработка эксперементальных данных Николаева Д.Р.(лаб), 612(Корпус 8/1 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%"> </td>

</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Проектирование автоматизированных систем. обработка информации и управления Антипова А.Н.(пр), 610(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Проектирование автоматизированных систем. обработка информации и управления Антипова А.Н.(л), 610(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Строительные конструкции вакансия (лаб), 062(Корпус 8/1 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%"> </td>

</tr></tbody></table>
</td>
	
</tr>


<tr>
	<td>3</td>
	<td>11:30 - 13:05</td>
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Проектирование автоматизированных систем. обработка информации и управления Лозикова И.О.(лаб), 517(Корпус 8/1 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%"> </td>

</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Надежность и качество информационных систем Паршуков А.Н.(пр), 704(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Информационная безопасность и защита информации Вяткин А.И.(л), 612(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Строительные конструкции вакансия (лаб), 062(Корпус 8/1 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%"> </td>

</tr></tbody></table>
</td>
	
</tr>


<tr>
	<td>4</td>
	<td>13:45 - 15:20</td>
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Информационная безопасность и защита информации Вяткин А.И.(лаб), а. 21 (БИЦ)()</td>

	<td class="stroke" width="50%"> </td>

</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">инженерные системы Максимова С.В.(пр), 521(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">инженерные системы Максимова С.В.(л), 414(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Проектирование автоматизированных систем. обработка информации и управления Лозикова И.О.(лаб), 612(Корпус 8/1 (ул. Луначарского, 2))</td>

	<td class="stroke" width="50%"> </td>

</tr></tbody></table>
</td>
	
</tr>


<tr>
	<td>5</td>
	<td>15:30 - 17:05</td>
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%">Информационная безопасность и защита информации Вяткин А.И.(лаб), а. 21 (БИЦ)()</td>

	<td class="stroke" width="50%"> </td>

</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Надежность и качество информационных систем Паршуков А.Н.(л), 612(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Иностранный язык (профессиональный) вакансия(пр), 459(Корпус 8 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
</tr>


<tr>
	<td>6</td>
	<td>17:15 - 18:50</td>
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Строительные конструкции вакансия (л), 061(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
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
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%"> </td>

	<td class="stroke" width="50%">Планирование эксперимента и обработка эксперементальных данных Николаева Д.Р.(лаб), 612(Корпус 8/1 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
</tr>


<tr>
	<td>2</td>
	<td>09:45 - 11:20</td>
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%"> </td>

	<td class="stroke" width="50%">Планирование эксперимента и обработка эксперементальных данных Николаева Д.Р.(лаб), 612(Корпус 8/1 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Проектирование автоматизированных систем. обработка информации и управления Антипова А.Н.(пр), 610(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%"> </td>

	<td class="stroke" width="50%">Строительные конструкции вакансия (лаб), 062(Корпус 8/1 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>
	
</tr>


<tr>
	<td>3</td>
	<td>11:30 - 13:05</td>
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%"> </td>

	<td class="stroke" width="50%">Проектирование автоматизированных систем. обработка информации и управления Лозикова И.О.(лаб), 517(Корпус 8/1 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Надежность и качество информационных систем Паршуков А.Н.(пр), 704(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Информационная безопасность и защита информации Вяткин А.И.(л), 612(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%"> </td>

	<td class="stroke" width="50%">Строительные конструкции вакансия (лаб), 062(Корпус 8/1 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>
	
</tr>


<tr>
	<td>4</td>
	<td>13:45 - 15:20</td>
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%"> </td>

	<td class="stroke" width="50%">Информационная безопасность и защита информации Вяткин А.И.(лаб), а. 21 (БИЦ)()</td>

</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">инженерные системы Максимова С.В.(пр), 521(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Иностранный язык (профессиональный) Филяровская Н.(пр), 271(Корпус 8 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">инженерные системы Максимова С.В.(л), 238(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%"> </td>

	<td class="stroke" width="50%">Проектирование автоматизированных систем. обработка информации и управления Лозикова И.О.(лаб), 612(Корпус 8/1 (ул. Луначарского, 2))</td>

</tr></tbody></table>
</td>
	
</tr>


<tr>
	<td>5</td>
	<td>15:30 - 17:05</td>
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="50%"> </td>

	<td class="stroke" width="50%">Информационная безопасность и защита информации Вяткин А.И.(лаб), а. 21 (БИЦ)()</td>

</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Планирование эксперимента и обработка эксперементальных данных Николаева Д.Р.(л), 602(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Иностранный язык (профессиональный) Филяровская Н.(пр), 459(Корпус 8 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
</tr>


<tr>
	<td>6</td>
	<td>17:15 - 18:50</td>
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
	
		<td>&nbsp;</td>
	
	
		<td><table class="cell" width="100%" height="100%">
<tbody><tr><td width="100%">Строительные конструкции вакансия (л), 061(Корпус 8/1 (ул. Луначарского, 2))</td>
</tr></tbody></table>
</td>
	
	
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
}
