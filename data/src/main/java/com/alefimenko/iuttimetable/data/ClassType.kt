package com.alefimenko.iuttimetable.data

enum class ClassType(
    val title: String,
    val color: String
) {
    Practicum("прак", "#ffbe0b"),
    Lecture("лек", "#fb5607"),
    LabWork("лаб", "#ff006e"),
    FakeExam("зач", "#8338ec"),
    Exam("экз", "#3a86ff"),
    Outside("ул", "#cb997e"),
    Unknown("---", "#4a4e69"),
}
