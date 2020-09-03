package com.dev.microservices.core.open.room.util;

import com.dev.microservices.core.open.room.document.OpenRoom;

import java.util.Calendar;
import java.util.function.BiFunction;

public class OpenRoomUtils {

    private OpenRoomUtils() {

    }

    public static BiFunction<Calendar, OpenRoom, OpenRoom> closeRoom = (calendar, openRoom) -> {
        openRoom.setEndDate(calendar);
        return openRoom;
    };

    public static BiFunction<Calendar, OpenRoom, OpenRoom> openDateRoom = (calendar, openRoom) -> {
        if (openRoom.getStartDate() == null) {
            openRoom.setStartDate(calendar);
        }
        return openRoom;
    };
}