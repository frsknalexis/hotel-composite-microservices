package com.dev.microservices.core.hotel.repository;

import com.dev.microservices.core.hotel.document.Hotel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("hotelRepository")
public interface HotelRepository extends ReactiveMongoRepository<Hotel, String> {

    Flux<Hotel> findByHotelId(String hotelId);

    Mono<Void> deleteByHotelId(String hotelId);
}