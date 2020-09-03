package com.dev.microservices.composite.hotel.service.impl;

import com.dev.microservices.composite.hotel.model.HotelRoom;
import com.dev.microservices.composite.hotel.service.HotelRoomBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service("hotelRoomBusinessService")
public class HotelRoomBusinessServiceImpl implements HotelRoomBusinessService {

    private final WebClient.Builder webClientBuilder;

    public HotelRoomBusinessServiceImpl(@Autowired WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<ResponseEntity<HotelRoom>> openRoom(HotelRoom room) {
        BodyInserter<Mono<HotelRoom>, ReactiveHttpOutputMessage> inserterBody = BodyInserters.
                fromPublisher(Mono.just(room), HotelRoom.class);

        String getHotelUri = "http://microservices-hotel/hotels?hotelId=".concat(room.getHotelId());
        String addRoomUri = "http://microservices-open-room/rooms";

        return webClientBuilder.build()
                .get().uri(getHotelUri).exchange()
                .filter(x -> x.statusCode() != OK)
                .flatMap(x -> Mono.just(ResponseEntity.status(x.statusCode())
                        .body((HotelRoom) null)))
                .switchIfEmpty(webClientBuilder.build().post().uri(addRoomUri)
                        .body(inserterBody).exchange()
                        .flatMap(x -> x.statusCode() != OK ?
                                Mono.just(ResponseEntity.status(x.statusCode()).build()) :
                                x.bodyToMono(HotelRoom.class)
                                        .flatMap(y -> Mono.just(ResponseEntity.status(OK).body(y)))
                        )
                )
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<HotelRoom>>> closeRoom(HotelRoom room) {
        ParameterizedTypeReference<List<HotelRoom>> type = new
                ParameterizedTypeReference<List<HotelRoom>>() {};

        String getHotelUri = "http://microservices-hotel/hotels?hotelId=".concat(room.getHotelId());
        String deleteRoomUri = "http://microservices-open-room/rooms?hotelId="+room.getHotelId()+
                "&reservationId="+room.getReservationId()+"&roomNumber="+room.getRoomNumber().toString();

        return webClientBuilder.build()
                .get().uri(getHotelUri).exchange()
                .filter(x -> x.statusCode() != OK)
                .flatMap(x -> Mono.just(ResponseEntity.status(x.statusCode())
                        .body((List<HotelRoom>) null)))
                .switchIfEmpty(webClientBuilder.build().delete().uri(deleteRoomUri).exchange()
                        .flatMap(x -> x.statusCode() != OK ?
                                Mono.just(ResponseEntity.status(x.statusCode()).build()) :
                                x.bodyToMono(type)
                                        .flatMap(hotelRooms -> Mono.just(ResponseEntity
                                                .status(OK).body(hotelRooms)))
                        )
                )
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<List<HotelRoom>>> findRoom(HotelRoom room) {
        ParameterizedTypeReference<List<HotelRoom>> type = new
                ParameterizedTypeReference<List<HotelRoom>>() {};

        String getHotelUri = "http://microservices-hotel/hotels?hotelId=".concat(room.getHotelId());
        String findRoomUri = "http://microservices-open-room/rooms?hotelId="+room.getHotelId()+
                "&reservationId="+room.getReservationId()+"&roomNumber="+room.getRoomNumber().toString();

        return webClientBuilder.build()
                .get().uri(getHotelUri).exchange()
                .filter(x -> x.statusCode() != OK)
                .flatMap(x -> Mono.just(ResponseEntity.status(x.statusCode())
                        .body((List<HotelRoom>) null)))
                .switchIfEmpty(webClientBuilder.build().get().uri(findRoomUri).exchange()
                        .flatMap(x -> x.statusCode() != OK ?
                                Mono.just(ResponseEntity.status(x.statusCode()).build()) :
                                x.bodyToMono(type)
                                        .flatMap(hotelRooms -> Mono.just(ResponseEntity.status(OK).body(hotelRooms)))
                        )
                )
                .onErrorResume(throwable -> Mono.just(ResponseEntity
                        .status(INTERNAL_SERVER_ERROR).build()));
    }
}