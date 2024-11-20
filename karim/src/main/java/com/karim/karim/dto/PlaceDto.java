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
    private String title;
    private String img1;
    private String addr1;
    private String addr2;
    private double latitude;
    private double longitude;
    private String planDate;
    private int order;
}
