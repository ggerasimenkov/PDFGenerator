package com.PDFGenerator.PDFGenerator.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DataFormatter {
    public static String format(String date)
    {
        return ZonedDateTime.parse(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }
}
