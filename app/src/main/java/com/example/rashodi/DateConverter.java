package com.example.rashodi;

import androidx.room.TypeConverter;
import java.util.Date;

// Класс для конвертации дат при работе с Room
public class DateConverter {
    // Конвертация из Long в Date
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    // Конвертация из Date в Long
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
} 