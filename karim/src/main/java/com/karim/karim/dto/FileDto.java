package com.karim.karim.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileDto {
    private int boardId;
    private String saveFolder;
    private String originalFile;
    private String saveFile;
}
