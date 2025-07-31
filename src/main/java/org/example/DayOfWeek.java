package org.example;

public enum DayOfWeek {
    MONDAY("Понедельник"),
    TUESDAY("Вторник"),
    WEDNESDAY("Среда"),
    THURSDAY("Четверг"),
    FRIDAY("Пятница"),
    SATURDAY("Суббота"),
    SUNDAY("Воскресенье");

    private final String translation;

    DayOfWeek (String translation) {
        this.translation = translation;
    }

    public String getTranslation(){
        return translation;
    }
}
