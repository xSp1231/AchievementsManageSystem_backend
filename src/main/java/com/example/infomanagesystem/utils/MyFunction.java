package com.example.infomanagesystem.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author xushupeng
 * @Date 2024-04-13 22:11
 */
public class MyFunction {
    public static String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return currentDateTime.format(formatter);
    }
}
