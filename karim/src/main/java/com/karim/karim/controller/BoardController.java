package com.karim.karim.controller;

import com.karim.karim.dto.BoardDto;
import com.karim.karim.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/board")
@Tag(name = "Board Controller", description = "게시판 정보 요청 API")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) { this.boardService = boardService; }

    @PostMapping()
    public ResponseEntity<String> save(@RequestBody BoardDto boardDto, @RequestBody MultipartFile[] files) throws Exception {
        int result = boardService.save(boardDto, files);
        if (result > 0) {
            return ResponseEntity.ok("계획 생성 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("계획 생성 실패");
        }
    }
}
