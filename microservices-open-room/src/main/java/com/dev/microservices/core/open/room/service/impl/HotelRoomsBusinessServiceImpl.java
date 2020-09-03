package com.dev.microservices.core.open.room.service.impl;

import com.dev.microservices.core.open.room.document.OpenRoom;
import com.dev.microservices.core.open.room.repository.OpenRoomRepository;
import com.dev.microservices.core.open.room.service.HotelRoomsBusinessService;
import com.dev.microservices.core.open.room.util.OpenRoomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.List;

import static com.dev.microservices.core.open.room.util.OpenRoomUtils.closeRoom;

@Service("hotelRoomsBusinessService")
public class HotelRoomsBusinessServiceImpl implements HotelRoomsBusinessService {

    private final OpenRoomRepository openRoomRepository;

    private final Calendar today;

    public HotelRoomsBusinessServiceImpl(@Autowired @Qualifier("openRoomRepository")
        OpenRoomRepository openRoomRepository) {
        this.openRoomRepository = openRoomRepository;
        this.today = Calendar.getInstance();
        this.today.set(Calendar.HOUR_OF_DAY, 0);
        this.today.set(Calendar.MINUTE, 0);
        this.today.set(Calendar.SECOND, 0);
        this.today.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public Mono<ResponseEntity<List<OpenRoom>>> getHotelRooms(String hotelId) {
        return openRoomRepository.findAllByHotelIdOrderByRoomNumber(hotelId)
                .filter(openRoom -> openRoom.getEndDate() == null || openRoom.getEndDate().after(today))
                .collectList()
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<OpenRoom>>> closeHotelsRoom(String hotelId) {
        return openRoomRepository.findAllByHotelIdOrderByRoomNumber(hotelId)
                .filter(openRoom -> openRoom.getEndDate() == null || openRoom.getEndDate().after(today))
                .map(this::apply)
                .flatMap(openRoomRepository::save)
                .collectList()
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    private OpenRoom apply(OpenRoom openRoom) {
        return closeRoom.apply(today, openRoom);
    }
}