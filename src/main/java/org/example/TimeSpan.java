package org.example;

import org.example.exceptions.IllegalInputTimeSpanException;
import org.example.exceptions.IllegalScaleFactorException;
import org.example.exceptions.NegativeTimeSpanException;

public class TimeSpan {
    private int totalMinutes;

    public TimeSpan() {
        this(0);
    }

    public TimeSpan(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException("Кількість хвилин має бути невід’ємною.");
        }
        totalMinutes = minutes;
    }

    public TimeSpan(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException("Введіть невід’ємні години та хвилини від 0 до 59.");
        }
        totalMinutes = checkedMinutes(60L * hours + minutes);
    }

    public TimeSpan(TimeSpan span) {
        this(minutesOf(span));
    }

    private static int minutesOf(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("Потрібно вказати інтервал.");
        }
        return span.totalMinutes;
    }

    private static int checkedMinutes(long value) {
        if (value > Integer.MAX_VALUE) {
            throw new IllegalInputTimeSpanException("Інтервал завеликий.");
        }
        return (int) value;
    }

    public int getHours() {
        return totalMinutes / 60;
    }

    public int getMinutes() {
        return totalMinutes % 60;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public double getTotalHours() {
        return totalMinutes / 60.0;
    }

    public void add(int hours, int minutes) {
        add(new TimeSpan(hours, minutes));
    }

    public void add(int minutes) {
        add(new TimeSpan(minutes));
    }

    public void add(TimeSpan span) {
        totalMinutes = checkedMinutes((long) totalMinutes + minutesOf(span));
    }

    public void subtract(int hours, int minutes) {
        subtract(new TimeSpan(hours, minutes));
    }

    public void subtract(int minutes) {
        subtract(new TimeSpan(minutes));
    }

    public void subtract(TimeSpan span) {
        int amount = minutesOf(span);
        if (amount > totalMinutes) {
            throw new NegativeTimeSpanException("Результат віднімання був би від’ємним.");
        }
        totalMinutes -= amount;
    }

    public void scale(int factor) {
        if (factor <= 0) {
            throw new IllegalScaleFactorException("Множник має бути більшим за нуль.");
        }
        totalMinutes = checkedMinutes((long) totalMinutes * factor);
    }
}
