package com.dev.microservices.core.open.room.repository;

import com.dev.microservices.core.open.room.document.OpenRoom;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("openRoomRepository")
public interface OpenRoomRepository extends ReactiveMongoRepository<OpenRoom, String> {

    Flux<OpenRoom> findAllByRoomNumber(Integer roomNumber);

    Flux<OpenRoom> findAllByReservationIdAndHotelIdOrderByRoomNumber(String reservationId, String hotelId);

    Flux<OpenRoom> findAllByHotelIdOrderByRoomNumber(String hotelId);

    Flux<OpenRoom> findByRoomNumberAndReservationIdAndHotelId(Integer roomNumber,
        String reservationId, String hotelId);

    @Query("{ 'hotelId' : ?2, 'reservationId' : ?1 },  'roomNumber' : ?0")
    Flux<OpenRoom> findRoom(Integer roomNumber, String reservationId, String hotelId);

    Mono<Void> deleteByRoomNumberAndHotelId(Integer roomNumber, String hotelId);

    Mono<Void> deleteAllByReservationId(String reservationId);

    Mono<Void> deleteAllByHotelId(String hotelId);
}