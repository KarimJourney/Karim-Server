package com.karim.karim.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDto {
    private int id;
    private long userId;
    private String nickname;
    private int boardId;
    private String content;
}
