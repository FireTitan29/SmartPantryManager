package com.example.smartpantrymanager.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class Validation {

    private Validation() {

    }

    // Validation for inputs, to be used when editing or adding pantry items

    public static boolean isValidString(String value) {

        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidBoolean(Boolean value) {

        return value != null;
    }

    public static boolean isValidFloat(float value) {

        return !Float.isNaN(value) && !Float.isInfinite(value);
    }

    public static boolean isValidFloat(float value, float minimum) {

        return isValidFloat(value) && value >= minimum;
    }

    public static boolean isValidInt(int value, int minimum, int maximum) {

        return value >= minimum && value <= maximum;
    }

    public static boolean isPositiveFloat(float value) {

        return isValidFloat(value) && value > 0;
    }

    // Validation of date field checker
    public static boolean isValidDate(String value) {

        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            dateFormat.setLenient(false);

            dateFormat.parse(value);

            return true;

        } catch (ParseException ex) {

            return false;
        }
    }

    public static boolean isPositiveInt(int value) {

        return value > 0;
    }
}