package com.karim.karim.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDto {
    private int id;
    private long userId; // 사용자 id (프로필 접속용)
    private String nickname; // 사용자 닉네임 (게시글 디스플레이용)
    private int place_id; // 올린 장소 id
    private int attraction_id; // 게시글에 약간 '나도 추가하기' 같은 느낌으로?
    private String title;
    private String content;
    private int hit;
    private String upload_date;
    private List<FileDto> files; // 파일 리스트
}