package com.dev.microservices.core.open.room.controller;

import com.dev.microservices.core.open.room.document.OpenRoom;
import com.dev.microservices.core.open.room.service.ReservationRoomsBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/reservations/rooms")
public class ReservationRoomsController {

    private final ReservationRoomsBusinessService reservationRoomsBusinessService;

    public ReservationRoomsController(@Autowired @Qualifier("reservationRoomsBusinessService")
        ReservationRoomsBusinessService reservationRoomsBusinessService) {
        this.reservationRoomsBusinessService = reservationRoomsBusinessService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<ResponseEntity<List<OpenRoom>>> findHotelRooms(@RequestParam(value = "hotelId") String hotelId,
        @RequestParam(value = "reservationId") String reservationId) {
        return reservationRoomsBusinessService.getReservationRooms(hotelId, reservationId);
    }

    @DeleteMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<OpenRoom>>> closeHotelRooms(@RequestParam(value = "hotelId") String hotelId,
        @RequestParam(value = "reservationId") String reservationId) {
        return reservationRoomsBusinessService.closeReservationRooms(hotelId, reservationId);
    }
}