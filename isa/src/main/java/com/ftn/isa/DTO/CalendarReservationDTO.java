package com.ftn.isa.DTO;

import com.ftn.isa.model.Reservation;

import java.time.format.DateTimeFormatter;

public class CalendarReservationDTO {

    private String reservationKind;
    private String startDate;
    private String endDate;

    public CalendarReservationDTO(){}

    public CalendarReservationDTO(Reservation reservation){
        if (reservation.isAction()){
            this.reservationKind = "Action reservation";
        }else if (reservation.isUnavailable()){
            this.reservationKind = "Period of unavailability";
        }else {
            this.reservationKind = "Reservation";
        }
        this.startDate = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.endDate   = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    }

    public CalendarReservationDTO(String reservationKind, String startDate, String endDate) {
        this.reservationKind = reservationKind;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getReservationKind() {
        return reservationKind;
    }

    public void setReservationKind(String reservationKind) {
        this.reservationKind = reservationKind;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
