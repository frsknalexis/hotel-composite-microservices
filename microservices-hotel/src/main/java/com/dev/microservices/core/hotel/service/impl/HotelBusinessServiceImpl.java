package com.dev.microservices.core.hotel.service.impl;

import com.dev.microservices.core.hotel.document.Hotel;
import com.dev.microservices.core.hotel.repository.HotelRepository;
import com.dev.microservices.core.hotel.service.HotelBusinessService;
import com.dev.microservices.core.hotel.util.HotelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.List;

@Service("hotelBusinessService")
public class HotelBusinessServiceImpl implements HotelBusinessService {

    private final HotelRepository hotelRepository;

    private Calendar today;

    public HotelBusinessServiceImpl(@Autowired @Qualifier("hotelRepository") HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
        this.today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public Mono<ResponseEntity<Hotel>> insertHotel(Hotel hotel) {
        return hotelRepository.findByHotelId(hotel.getHotelId())
                .filter(h -> HotelUtils.isValidHotelLambda.apply(today, h))
                .hasElements()
                .filter(x -> !x.booleanValue())
                .flatMap(x -> Mono.just(hotel))
                .flatMap(h -> hotelRepository.save(h))
                .map(h -> ResponseEntity.status(HttpStatus.CREATED).body(h))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<Hotel>>> getHotel(String hotelId) {
        return hotelRepository.findByHotelId(hotelId)
                .filter(h -> HotelUtils.isValidHotelLambda.apply(today, h))
                .collectList()
                .filter(hotels -> hotels != null && !hotels.isEmpty())
                .flatMap(hotels -> Mono.just(ResponseEntity.ok().body(hotels)))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<Hotel>>> updateHotel(Hotel hotel) {
        return hotelRepository.findByHotelId(hotel.getHotelId())
                .filter(h -> HotelUtils.isValidHotelLambda.apply(today, h))
                .flatMap(h -> hotelRepository.save(h))
                .collectList()
                .map(hotels -> ResponseEntity.ok().body(hotels))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<Hotel>>> closeHotel(String hotelId) {
        return hotelRepository.findByHotelId(hotelId)
                .filter(h -> HotelUtils.isValidHotelLambda.apply(today, h))
                .filter(h -> h != null)
                .map(h -> HotelUtils.closeHotelLambda.apply(today, h))
                .flatMap(h -> hotelRepository.save(h))
                .collectList()
                .map(hotels -> ResponseEntity.ok().body(hotels))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<Hotel>>> getAllHotels() {
        return hotelRepository.findAll()
                .filter(h -> HotelUtils.isValidHotelLambda.apply(today, h))
                .collectList()
                .filter(h -> h != null && !h.isEmpty())
                .flatMap(hotels -> Mono.just(ResponseEntity.ok().body(hotels)))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<Hotel>>> closeAllHotels() {
        return hotelRepository.findAll()
                .filter(h -> HotelUtils.isValidHotelLambda.apply(today, h))
                .map(h -> HotelUtils.closeHotelLambda.apply(today, h))
                .flatMap(h -> hotelRepository.save(h))
                .collectList()
                .flatMap(hotels -> Mono.just(ResponseEntity.ok().body(hotels)))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }
}