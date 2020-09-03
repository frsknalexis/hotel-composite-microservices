package com.dev.microservices.core.open.room.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Calendar;

@Document(collection = "open_rooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenRoom implements Serializable {

    @Id
    private String id;

    private Integer roomNumber;

    private String hotelId;

    private String reservationId;

    private String password;

    private Calendar startDate;

    private Calendar endDate;
}