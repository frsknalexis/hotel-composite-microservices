package com.dev.microservices.core.hotel.service;

import com.dev.microservices.core.hotel.document.Hotel;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface HotelAddressBusinessService {

    Mono<ResponseEntity<List<Hotel>>> updateHotelAddress(Hotel hotel);
}