package com.karim.karim.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlaceDto {
    private int id;
    private int planId;
    private int attrId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String planDate;
    private int order;
    private long cost;
    private String content;
}
