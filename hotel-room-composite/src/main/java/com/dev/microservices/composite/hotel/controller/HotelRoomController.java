package com.dev.microservices.composite.hotel.controller;

import com.dev.microservices.composite.hotel.model.HotelRoom;
import com.dev.microservices.composite.hotel.service.HotelRoomBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/hotelrooms")
public class HotelRoomController {

    private final HotelRoomBusinessService hotelRoomBusinessService;

    public HotelRoomController(@Autowired @Qualifier("hotelRoomBusinessService")
        HotelRoomBusinessService hotelRoomBusinessService) {
        this.hotelRoomBusinessService = hotelRoomBusinessService;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE },
        produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<HotelRoom>> insertNewRoom(@RequestBody Mono<HotelRoom> room) {
        return room.flatMap(r -> hotelRoomBusinessService.openRoom(r));
    }

    @DeleteMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<HotelRoom>>> closeRoom(@RequestParam(value = "roomNumber") String roomNumber,
        @RequestParam(value = "hotelId") String hotelId, @RequestParam(value = "reservationId") String reservationId) {
        HotelRoom hotelRoom = HotelRoom.builder()
                .hotelId(hotelId)
                .reservationId(reservationId)
                .roomNumber(Integer.valueOf(roomNumber))
                .build();
        return hotelRoomBusinessService.closeRoom(hotelRoom);
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<HotelRoom>>> findRoom(ServerHttpRequest request) {
        HotelRoom hotelRoom = HotelRoom.builder()
                .hotelId(request.getQueryParams().getFirst("hotelId"))
                .reservationId(request.getQueryParams().getFirst("reservationId"))
                .roomNumber(Integer.valueOf(request.getQueryParams().getFirst("roomNumber")))
                .build();
        return hotelRoomBusinessService.findRoom(hotelRoom);
    }
}