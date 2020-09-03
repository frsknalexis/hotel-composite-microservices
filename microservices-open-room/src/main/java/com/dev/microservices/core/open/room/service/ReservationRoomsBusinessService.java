package com.dev.microservices.core.open.room.service;

import com.dev.microservices.core.open.room.document.OpenRoom;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReservationRoomsBusinessService {

    Mono<ResponseEntity<List<OpenRoom>>> getReservationRooms(String hotelId, String reservationId);

    Mono<ResponseEntity<List<OpenRoom>>> closeReservationRooms(String hotelId, String reservationId);
}