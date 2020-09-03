package com.dev.microservices.core.hotel.service.impl;

import com.dev.microservices.core.hotel.document.Hotel;
import com.dev.microservices.core.hotel.repository.HotelRepository;
import com.dev.microservices.core.hotel.service.HotelAddressBusinessService;
import com.dev.microservices.core.hotel.util.HotelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.List;

@Service("hotelAddressBusinessService")
public class HotelAddressBusinessServiceImpl implements HotelAddressBusinessService {

    private Calendar today;

    private final HotelRepository hotelRepository;

    public HotelAddressBusinessServiceImpl(@Autowired @Qualifier("hotelRepository") HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
        this.today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public Mono<ResponseEntity<List<Hotel>>> updateHotelAddress(Hotel hotel) {
        return hotelRepository.findByHotelId(hotel.getHotelId())
                .filter(h -> HotelUtils.isValidHotelLambda.apply(today, h))
                .map(h -> {
                    h.setHotelLocation(hotel.getHotelLocation());
                    return h;
                })
                .flatMap(h -> hotelRepository.save(h))
                .collectList()
                .flatMap(hotels -> Mono.just(ResponseEntity.ok().body(hotels)))
                .switchIfEmpty(Mono.just(ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED).build()))
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }
}