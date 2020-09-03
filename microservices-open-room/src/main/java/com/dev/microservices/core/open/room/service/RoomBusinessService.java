package com.dev.microservices.core.open.room.service;

import com.dev.microservices.core.open.room.document.OpenRoom;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoomBusinessService {

    Mono<ResponseEntity<OpenRoom>> openRoom(OpenRoom openRoom);

    Mono<ResponseEntity<List<OpenRoom>>> getRoom(OpenRoom openRoom);

    Mono<ResponseEntity<List<OpenRoom>>> updateRoom(OpenRoom openRoom);

    Mono<ResponseEntity<List<OpenRoom>>> closeRoom(OpenRoom openRoom);

    Mono<ResponseEntity<List<OpenRoom>>> getAllRooms();

    Mono<ResponseEntity<List<OpenRoom>>> openMultipleRooms(List<OpenRoom> openRooms);
}