package com.ftn.isa.helpers;

import com.ftn.isa.DTO.ReservingInfoDTO;
import com.ftn.isa.model.Reservation;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Validate {
    private static final String REG_NAME = "^[a-zA-Z\\s]+";
    private static final String REG_EMAIL = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+$";
    private static final String REG_NUMBER = "^[0-9]+$";
    private static final String REG_PASSWORD = "^[a-zA-Z0-9!@#$%^&*()_+=]{6,30}$";
    private static final String REG_STREET = "^[a-zA-Z0-9 -.]+$";
    private static final String REG_WORDS = "^[a-zA-Z -]+$";
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static LocalDateTime getTodaysDate() {
        return LocalDateTime.now();
    }

    public static LocalDateTime getCurrentWeekMonday() {
        LocalDateTime today = LocalDateTime.now();

        LocalDateTime monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        return monday.withHour(0).withMinute(1).withSecond(1);
    }

    public static LocalDateTime getCurrentWeekSunday() {
        LocalDateTime today = LocalDateTime.now();

        // Go forward to get Sunday
        LocalDateTime sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }

        return sunday.withHour(0).withMinute(1).withSecond(1);
    }

    public static LocalDateTime getCurrentMonthBegining() {
        return LocalDateTime.now().withDayOfMonth(1);
    }

    public static LocalDateTime getCurrentMonthEnd() {
        return LocalDateTime.now().plusMonths(1).withDayOfMonth(1).minusDays(1);
    }

    public static LocalDateTime getSelectedMonthStart(String selectedMonth) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = getSelectedMonth(selectedMonth);
        int day = 1;
        c.set(year, month, day);
        return LocalDateTime.ofInstant(c.toInstant(), c.getTimeZone().toZoneId()).toLocalDate().atTime(00,00);
    }

    public static LocalDateTime getSelectedMonthEnd(String selectedMonth) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = getSelectedMonth(selectedMonth);
        int day = 1;
        c.set(year, month, day);
        int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
        return LocalDateTime.ofInstant(c.toInstant(), c.getTimeZone().toZoneId()).toLocalDate().atTime(00,00);
    }

    public static int getSelectedMonth(String month) {
        switch (month.toUpperCase()){
            case "JANUARY": return Calendar.JANUARY;
            case "FEBRUARY": return Calendar.FEBRUARY;
            case "MARCH": return Calendar.MARCH;
            case "APRIL": return Calendar.APRIL;
            case "MAY": return Calendar.MAY;
            case "JUNE": return Calendar.JUNE;
            case "JULY": return Calendar.JULY;
            case "AUGUST": return Calendar.AUGUST;
            case "SEPTEMBER": return Calendar.SEPTEMBER;
            case "OCTOBER": return Calendar.OCTOBER;
            case "NOVEMBER": return Calendar.NOVEMBER;
            case "DECEMBER": return Calendar.DECEMBER;
        }
        return Calendar.JANUARY;
    }

    public static LocalDateTime getStartOfYear() {
        int year = 2022;  // you can pass any value of year Like 2020,2021...
        int month = 1;   // you can pass any value of month Like 1,2,3...
        YearMonth yearMonth = YearMonth.of( year, month );
        LocalDate firstOfMonth = yearMonth.atDay( 1 );

        return firstOfMonth.atStartOfDay();
    }

    public static LocalDateTime getEndOfYear() {
        int year = 2022;  // you can pass any value of year Like 2020,2021...
        int month = 12;   // you can pass any value of month Like 1,2,3...
        YearMonth yearMonth = YearMonth.of( year, month );
        LocalDate lastOfMonth = yearMonth.atEndOfMonth();

        return lastOfMonth.atStartOfDay();
    }

    public static boolean validateSurName(String surName) {
        return surName != null && surName.matches(REG_NAME);
    }

    public static boolean validateEmail(String email) {
        return email != null && email.matches(REG_EMAIL);
    }

    public static boolean validateNumber(String toVal) {
        return toVal != null && toVal.matches(REG_NUMBER);
    }

    public static boolean validatePassword(String password) {
        return password != null && password.matches(REG_PASSWORD);
    }

    public static boolean validateWords(String words) {
        return words != null && words.matches(REG_WORDS);
    }

    public static boolean validateStreet(String street) {
        return street != null && street.matches(REG_STREET);
    }

    public static boolean validateDatePeriod(String startDate, String endDate) {
        if (startDate.equals("") || endDate.equals(""))
            return false;

        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date stDate = sf.parse(startDate);
            Date enDate = sf.parse(endDate);

            return !(stDate.before(new Date()) || enDate.before(new Date()) || enDate.before(stDate));
        } catch (Exception e) { return false; }
    }

    public static boolean validatePrice(String price) {
        try {
            if (!price.equals(""))
                Double.parseDouble(price);
        } catch (Exception e) { return false; }

        return true;
    }

    public static boolean validateRating(String rating) {
        return Arrays.asList("", "1", "2", "3", "4", "5").contains(rating);
    }

    public static boolean validateSearchEntities(String entities) {
        return entities.contains("adventures") || entities.contains("cottages") || entities.contains("boats");
    }

    public static boolean validateIfReservationPeriodIsAvailable(List<Reservation> reservations, ReservingInfoDTO reservingData) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(reservingData.getStartDate(), format);
        LocalDateTime endTime = LocalDateTime.parse(reservingData.getEndDate(), format);

        for (Reservation res : reservations)
            if (res.periodsAreOverlapping(startTime, endTime))
                return false;
        return true;
    }

    public static boolean validateIfResPeriodWasCanceled(Set<Reservation> reservations, ReservingInfoDTO reservingData) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(reservingData.getStartDate(), format);
        LocalDateTime endTime = LocalDateTime.parse(reservingData.getEndDate(), format);

        for (Reservation res : reservations)
            if (res.isCanceled() && res.periodsAreOverlapping(startTime, endTime))
                return false;
        return true;
    }

}