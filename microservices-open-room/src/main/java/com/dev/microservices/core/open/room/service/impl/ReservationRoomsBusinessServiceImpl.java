package com.dev.microservices.core.open.room.service.impl;

import com.dev.microservices.core.open.room.document.OpenRoom;
import com.dev.microservices.core.open.room.repository.OpenRoomRepository;
import com.dev.microservices.core.open.room.service.ReservationRoomsBusinessService;
import com.dev.microservices.core.open.room.util.OpenRoomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.List;

@Service("reservationRoomsBusinessService")
public class ReservationRoomsBusinessServiceImpl implements ReservationRoomsBusinessService {

    private final OpenRoomRepository openRoomRepository;

    private final Calendar today;

    public ReservationRoomsBusinessServiceImpl(@Autowired @Qualifier("openRoomRepository")
        OpenRoomRepository openRoomRepository) {
        this.openRoomRepository = openRoomRepository;
        this.today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public Mono<ResponseEntity<List<OpenRoom>>> getReservationRooms(String hotelId, String reservationId) {
        return openRoomRepository.findAllByReservationIdAndHotelIdOrderByRoomNumber(reservationId, hotelId)
                .filter(openRoom -> openRoom.getEndDate() == null || openRoom.getEndDate().after(today))
                .collectList()
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<OpenRoom>>> closeReservationRooms(String hotelId, String reservationId) {
        return openRoomRepository.findAllByReservationIdAndHotelIdOrderByRoomNumber(reservationId, hotelId)
                .filter(openRoom -> openRoom.getEndDate() == null || openRoom.getEndDate().after(today))
                .map(openRoom -> OpenRoomUtils.closeRoom.apply(today, openRoom))
                .flatMap(openRoom -> openRoomRepository.save(openRoom))
                .collectList()
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }
}