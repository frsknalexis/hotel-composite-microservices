package com.dev.microservices.core.hotel.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location implements Serializable {

    private String city;

    private String state;

    private String coordinates;
}