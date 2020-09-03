package com.dev.microservices.composite.hotel.service;

import com.dev.microservices.composite.hotel.model.HotelRoom;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface HotelRoomBusinessService {

    Mono<ResponseEntity<HotelRoom>> openRoom(HotelRoom room);

    Mono<ResponseEntity<List<HotelRoom>>> closeRoom(HotelRoom room);

    Mono<ResponseEntity<List<HotelRoom>>> findRoom(HotelRoom room);
}