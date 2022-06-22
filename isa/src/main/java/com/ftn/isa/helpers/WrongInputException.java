package com.ftn.isa.helpers;

public class WrongInputException extends Exception {

    private String valueName;

    public WrongInputException(String valueName) {
        this.valueName = valueName;
    }

    public String getValueName() {
        return valueName;
    }
}

