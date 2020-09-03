package com.dev.microservices.core.open.room.controller;

import com.dev.microservices.core.open.room.document.OpenRoom;
import com.dev.microservices.core.open.room.service.HotelRoomsBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/hotels/rooms")
public class HotelRoomsController {

    private final HotelRoomsBusinessService hotelRoomsBusinessService;

    public HotelRoomsController(@Autowired @Qualifier("hotelRoomsBusinessService")
        HotelRoomsBusinessService hotelRoomsBusinessService) {
        this.hotelRoomsBusinessService = hotelRoomsBusinessService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<ResponseEntity<List<OpenRoom>>> findHotelRooms(@RequestParam(value = "hotelId")
        String hotelId) {
        return hotelRoomsBusinessService.getHotelRooms(hotelId);
    }

    @DeleteMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<OpenRoom>>> closeHotelRooms(@RequestParam(value = "hotelId")
        String hotelId) {
        return hotelRoomsBusinessService.closeHotelsRoom(hotelId);
    }
}