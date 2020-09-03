package com.dev.microservices.core.hotel.service;

import com.dev.microservices.core.hotel.document.Hotel;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface HotelBusinessService {

    Mono<ResponseEntity<Hotel>> insertHotel(Hotel hotel);

    Mono<ResponseEntity<List<Hotel>>> getHotel(String hotelId);

    Mono<ResponseEntity<List<Hotel>>> updateHotel(Hotel hotel);

    Mono<ResponseEntity<List<Hotel>>> closeHotel(String hotelId);

    Mono<ResponseEntity<List<Hotel>>> getAllHotels();

    Mono<ResponseEntity<List<Hotel>>> closeAllHotels();
}
