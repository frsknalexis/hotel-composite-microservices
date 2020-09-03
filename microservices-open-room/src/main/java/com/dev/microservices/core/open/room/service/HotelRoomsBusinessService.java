package com.dev.microservices.core.open.room.service;

import com.dev.microservices.core.open.room.document.OpenRoom;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface HotelRoomsBusinessService {

    Mono<ResponseEntity<List<OpenRoom>>> getHotelRooms(String hotelId);

    Mono<ResponseEntity<List<OpenRoom>>> closeHotelsRoom(String hotelId);
}