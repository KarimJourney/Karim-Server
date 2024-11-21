package com.karim.karim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HelpDto {
    private int id;           // 고유 ID
    private String question;  // 질문
    private String answer;    // 답변
}
