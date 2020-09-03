package com.dev.microservices.core.hotel.controller;

import com.dev.microservices.core.hotel.document.Hotel;
import com.dev.microservices.core.hotel.service.HotelBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelBusinessService hotelBusinessService;

    public HotelController(@Autowired @Qualifier("hotelBusinessService") HotelBusinessService hotelBusinessService) {
        this.hotelBusinessService = hotelBusinessService;
    }

    @PostMapping(value = "/insert", consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<Hotel>> insertHotel(@RequestBody Mono<Hotel> hotelRequest) {
        return hotelRequest.flatMap(hotel -> hotelBusinessService.insertHotel(hotel));
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<Hotel>>> getHotel(@RequestParam(value = "hotelId") String hotelId) {
        return hotelBusinessService.getHotel(hotelId);
    }

    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE },
        produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<Hotel>>> updateHotel(@RequestBody Mono<Hotel> hotelRequest) {
        return hotelRequest.flatMap(hotel -> hotelBusinessService.updateHotel(hotel));
    }

    @DeleteMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<Hotel>>> closeHotel(@RequestParam(value = "hotelId") String hotelId) {
        return hotelBusinessService.closeHotel(hotelId);
    }

    @GetMapping(value = "/all", produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<Hotel>>> getAllHotels() {
        return hotelBusinessService.getAllHotels();
    }

    @DeleteMapping(value = "/all", produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<Hotel>>> closeAllHotel() {
        return hotelBusinessService.closeAllHotels();
    }
}