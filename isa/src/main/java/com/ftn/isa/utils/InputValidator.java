package com.ftn.isa.utils;

public class InputValidator {

    public InputValidator() {

    }

    public static boolean checkIfEmpty(String value) {
        return value == null || value.equals("");
    }

    public static boolean containDigit(String value) {
        return value.matches(".*\\d.*");	//vraca true kada sadrzi bar 1 broj
    }

    public static boolean containOnlyDigits(String value) {
        return value.matches("^[0-9]+$");
    }

    public static boolean isFloat(String value) {
        float f = 0;
        try {
            f = Float.parseFloat(value);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean isInteger(String value) {
        int number = 0;
        try {
            number = Integer.parseInt(value);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean isStrongPassword(String value) {
        int counter = 0;
        if (value.length() < 5) {
            return false;
        }
        else if (!containDigit(value)) {
            return false;
        }
        else if ((counter = numberOfSpecialSymbols(value, "!@#$%&*")) < 1) {
            return false;
        }
        return true;
    }

    public static int numberOfSpecialSymbols(String value, String specialSymbols) {
        int counter = 0;
        for (int i=0; i < value.length() ; i++)
        {
            char ch = value.charAt(i);
            if(specialSymbols.contains(Character.toString(ch))) {
                counter++;
            }
        }
        return counter;
    }

}
