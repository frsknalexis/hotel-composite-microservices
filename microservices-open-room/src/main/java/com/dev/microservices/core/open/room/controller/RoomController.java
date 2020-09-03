package com.dev.microservices.core.open.room.controller;

import com.dev.microservices.core.open.room.document.OpenRoom;
import com.dev.microservices.core.open.room.service.RoomBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomBusinessService roomBusinessService;

    public RoomController(@Autowired @Qualifier("roomBusinessService")
        RoomBusinessService roomBusinessService) {
        this.roomBusinessService = roomBusinessService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<OpenRoom>> insertRoom(@RequestBody Mono<OpenRoom> openRoomRequest) {
        return openRoomRequest.flatMap(openRoom -> roomBusinessService.openRoom(openRoom));
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<OpenRoom>>> findRoom(@RequestParam(value = "roomNumber") String roomNumber,
        @RequestParam(value = "hotelId") String hotelId, @RequestParam(value = "reservationId") String reservationId) {
        OpenRoom openRoom = OpenRoom.builder()
                .hotelId(hotelId)
                .reservationId(reservationId)
                .roomNumber(Integer.valueOf(roomNumber))
                .build();
        return roomBusinessService.getRoom(openRoom);
    }

    @GetMapping(value = "/all", produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<OpenRoom>>> findAllRooms() {
        return roomBusinessService.getAllRooms();
    }

    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE },
        produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<OpenRoom>>> updateRoom(@RequestBody Mono<OpenRoom> openRoomRequest) {
        return openRoomRequest.flatMap(openRoom -> roomBusinessService.updateRoom(openRoom));
    }

    @DeleteMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<OpenRoom>>> closeRoom(@RequestParam(value = "roomNumber") String roomNumber,
        @RequestParam(value = "hotelId") String hotelId, @RequestParam(value = "reservationId") String reservationId) {
        OpenRoom openRoom = OpenRoom.builder()
                .hotelId(hotelId)
                .reservationId(reservationId)
                .roomNumber(Integer.valueOf(roomNumber))
                .build();
        return roomBusinessService.closeRoom(openRoom);
    }
}