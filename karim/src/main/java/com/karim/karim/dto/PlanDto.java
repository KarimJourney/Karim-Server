package com.karim.karim.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlanDto {
    private int id;
    private long userId;
    private String name;
    private String startDate;
    private String endDate;
    private long cost;
    private String content;
}
