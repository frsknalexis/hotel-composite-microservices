package com.dev.microservices.core.hotel.util;

import com.dev.microservices.core.hotel.document.Hotel;

import java.util.Calendar;
import java.util.function.BiFunction;

public class HotelUtils {

    public static BiFunction<Calendar, Hotel, Hotel> closeHotelLambda = (calendar, hotel) -> {
        hotel.setClosingDate(calendar);
        return hotel;
    };

    public static BiFunction<Calendar, Hotel, Boolean> isValidHotelLambda =
            (calendar, hotel) -> hotel.getClosingDate() == null || hotel.getClosingDate().after(calendar);
}