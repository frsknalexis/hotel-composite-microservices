package com.dev.microservices.core.hotel.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Calendar;

@Document(collection = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel implements Serializable {

    @Id
    private String id;

    private String hotelId;

    private String hotelName;

    private Location hotelLocation;

    private Calendar closingDate;
}