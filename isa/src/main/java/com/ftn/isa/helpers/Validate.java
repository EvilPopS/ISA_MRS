package com.ftn.isa.helpers;

import com.ftn.isa.DTO.ReservingInfoDTO;
import com.ftn.isa.model.Reservation;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Validate {
    private static final String REG_NAME = "^[a-zA-Z\\s]+";
    private static final String REG_EMAIL = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+$";
    private static final String REG_NUMBER = "^[0-9]+$";
    private static final String REG_PASSWORD = "^[a-zA-Z0-9!@#$%^&*()_+=]{6,30}$";
    private static final String REG_STREET = "^[a-zA-Z0-9 -.]+$";
    private static final String REG_WORDS = "^[a-zA-Z -]+$";
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static LocalDate getTodaysDate() {
        return LocalDate.now();
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
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startTime = LocalDate.parse(reservingData.getStartDate(), format);
        LocalDate endTime = LocalDate.parse(reservingData.getEndDate(), format);

        for (Reservation res : reservations)
            if (res.periodsAreOverlapping(startTime, endTime))
                return false;
        return true;
    }
}