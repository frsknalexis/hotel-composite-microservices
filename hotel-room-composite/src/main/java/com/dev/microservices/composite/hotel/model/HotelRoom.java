package com.dev.microservices.composite.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelRoom implements Serializable {

    private static final long serialVersionUID = 358632845812756345L;

    private String hotelId;

    private String reservationId;

    private Integer roomNumber;

    private String password;

    private Calendar startDate;

    private Calendar endDate;
}