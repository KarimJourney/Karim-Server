package com.karim.karim.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karim.karim.dto.BoardDto;
import com.karim.karim.service.BoardService;
import com.karim.karim.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/board")
@Tag(name = "Board Controller", description = "게시판 관련 API")
public class BoardController {

    private final BoardService boardService;
    private final JWTUtil jwtUtil;

    public BoardController(BoardService boardService, JWTUtil jwtUtil) {
        this.boardService = boardService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @PostMapping("/write")
    public ResponseEntity<String> write(
            @RequestPart("board") String boardString,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String accessToken) {

        System.out.println("Received board data: " + boardString);
        System.out.println("Received files: " + Arrays.toString(files));
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        validateAccessToken(accessToken);

        String tokenId = jwtUtil.getId(accessToken);

        BoardDto boardDto = parseBoardDto(boardString);
        boardDto.setUserId(Long.parseLong(tokenId));

        try {
            int result = boardService.save(boardDto, files);
            return result > 0 ? ResponseEntity.ok("게시글 작성 성공") : ResponseEntity.ok("게시글 작성 성공 (파일 없음)");
        } catch (Exception e) {
            throw new IllegalStateException("게시글 작성 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "게시글 목록 조회", description = "모든 게시글 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<List<BoardDto>> list() {
        try {
            List<BoardDto> boardList = boardService.findAll();
            return ResponseEntity.ok(boardList);
        } catch (Exception e) {
            throw new IllegalStateException("게시글 목록 조회 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 ID를 통해 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> detail(@PathVariable("id") int id) {
        try {
            BoardDto boardDto = boardService.findByBoardId(id);
            if (boardDto == null) {
                throw new IllegalStateException("게시글 조회 실패: 해당 게시글을 찾을 수 없습니다.");
            }
            boardService.modifyHit(id);
            return ResponseEntity.ok(boardDto);
        } catch (Exception e) {
            throw new IllegalStateException("게시글 상세 조회 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @PatchMapping("/modify")
    public ResponseEntity<String> modify(
            @RequestPart("board") String boardString,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String accessToken) {

        validateAccessToken(accessToken);

        String tokenId = jwtUtil.getId(accessToken);
        BoardDto boardDto = parseBoardDto(boardString);

        if (boardDto.getUserId() != Long.parseLong(tokenId)) {
            throw new IllegalStateException("AccessToken의 사용자와 수정 요청된 작성자가 일치하지 않습니다.");
        }

        try {
            int result = boardService.modify(boardDto, files);
            return result > 0 ? ResponseEntity.ok("게시글 수정 성공") : ResponseEntity.ok("게시글 수정 성공 (파일 없음)");
        } catch (Exception e) {
            throw new IllegalStateException("게시글 수정 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(
            @PathVariable int id,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String accessToken) {

        validateAccessToken(accessToken);

        String tokenId = jwtUtil.getId(accessToken);
        BoardDto boardDto = boardService.findByBoardId(id);

        if (boardDto == null) {
            throw new IllegalStateException("게시글 삭제 실패: 해당 게시글을 찾을 수 없습니다.");
        }

        if (boardDto.getUserId() != Long.parseLong(tokenId)) {
            throw new IllegalStateException("AccessToken의 사용자와 삭제 요청된 작성자가 일치하지 않습니다.");
        }

        try {
            int result = boardService.delete(id);
            return result > 0 ? ResponseEntity.ok("게시글 삭제 성공") : ResponseEntity.ok("게시글 삭제 실패");
        } catch (Exception e) {
            throw new IllegalStateException("게시글 삭제 실패: " + e.getMessage());
        }
    }

    private void validateAccessToken(String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new IllegalStateException("AccessToken이 제공되지 않았거나 유효하지 않습니다.");
        }
    }

    private BoardDto parseBoardDto(String boardString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(boardString, BoardDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("board 데이터를 파싱하는 데 실패했습니다: " + e.getMessage());
        }
    }
}
