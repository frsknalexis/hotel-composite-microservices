package com.dev.microservices.core.open.room.service.impl;

import com.dev.microservices.core.open.room.document.OpenRoom;
import com.dev.microservices.core.open.room.repository.OpenRoomRepository;
import com.dev.microservices.core.open.room.service.RoomBusinessService;
import com.dev.microservices.core.open.room.util.OpenRoomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.List;

@Service("roomBusinessService")
public class RoomBusinessServiceImpl implements RoomBusinessService {

    private final OpenRoomRepository openRoomRepository;

    private final Calendar today;

    public RoomBusinessServiceImpl(@Autowired @Qualifier("openRoomRepository")
        OpenRoomRepository openRoomRepository) {
        this.openRoomRepository = openRoomRepository;
        this.today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public Mono<ResponseEntity<OpenRoom>> openRoom(OpenRoom openRoom) {
        return openRoomRepository.findByRoomNumberAndReservationIdAndHotelId(openRoom.getRoomNumber(),
                openRoom.getReservationId(), openRoom.getHotelId())
                .filter(x -> x.getEndDate() == null || x.getEndDate().after(today))
                .hasElements()
                .filter(x -> !x.booleanValue())
                .flatMap(x -> Mono.just(openRoom))
                .map(this::apply)
                .flatMap(openRoomRepository::save)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity.
                        status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<OpenRoom>>> getRoom(OpenRoom openRoom) {
        return openRoomRepository.findByRoomNumberAndReservationIdAndHotelId(openRoom.getRoomNumber(),
                openRoom.getReservationId(), openRoom.getHotelId())
                .filter(x -> x.getEndDate() == null || x.getEndDate().after(today))
                .collectList()
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<OpenRoom>>> updateRoom(OpenRoom openRoom) {
        return openRoomRepository.findByRoomNumberAndReservationIdAndHotelId(openRoom.getRoomNumber(),
                openRoom.getReservationId(), openRoom.getHotelId())
                .filter(x -> x.getEndDate() == null || x.getEndDate().after(today))
                .flatMap(openRoomRepository::save)
                .collectList()
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<OpenRoom>>> closeRoom(OpenRoom openRoom) {
        return openRoomRepository.findByRoomNumberAndReservationIdAndHotelId(openRoom.getRoomNumber(),
                openRoom.getReservationId(), openRoom.getHotelId())
                .filter(x -> x.getEndDate() == null || x.getEndDate().after(today))
                .map(x -> OpenRoomUtils.closeRoom.apply(today, x))
                .flatMap(openRoomRepository::save)
                .collectList()
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<OpenRoom>>> getAllRooms() {
        return openRoomRepository.findAll()
                .filter(openRoom -> openRoom.getEndDate() ==  null || openRoom.getEndDate().after(today))
                .collectList()
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<OpenRoom>>> openMultipleRooms(List<OpenRoom> openRooms) {
        return Flux.fromIterable(openRooms)
                .map(openRoom -> OpenRoomUtils.openDateRoom.apply(today, openRoom))
                .flatMap(openRoom -> openRoomRepository.save(openRoom))
                .collectList()
                .map(lista -> ResponseEntity.ok().body(lista))
                .switchIfEmpty(Mono.just(ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    private OpenRoom apply(OpenRoom x) {
        return OpenRoomUtils.openDateRoom.apply(today, x);
    }
}