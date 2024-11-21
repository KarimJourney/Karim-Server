package com.karim.karim.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttractionDto {
    private int id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
}
