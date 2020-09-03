package com.dev.microservices.core.hotel.controller;

import com.dev.microservices.core.hotel.document.Hotel;
import com.dev.microservices.core.hotel.service.HotelAddressBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/hotels/address")
public class HotelAddressController {

    private final HotelAddressBusinessService hotelAddressBusinessService;

    public HotelAddressController(@Autowired @Qualifier("hotelAddressBusinessService") HotelAddressBusinessService hotelAddressBusinessService) {
        this.hotelAddressBusinessService = hotelAddressBusinessService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = { MediaType.APPLICATION_JSON_VALUE })
    public Mono<ResponseEntity<List<Hotel>>> updateHotelAddress(@RequestBody Mono<Hotel> hotelRequest) {
        return hotelRequest.flatMap(hotel -> hotelAddressBusinessService.updateHotelAddress(hotel));
    }
}