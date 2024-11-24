package com.karim.karim.controller;

import com.karim.karim.dto.CommentDto;
import com.karim.karim.service.CommentService;
import com.karim.karim.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/comment")
@Tag(name = "Comment Controller", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;
    private final JWTUtil jwtUtil;

    public CommentController(CommentService commentService, JWTUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "댓글 작성", description = "새로운 댓글을 작성합니다.")
    @PostMapping("/write")
    public ResponseEntity<String> write(
            @RequestBody CommentDto commentDto,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String accessToken) {

        jwtUtil.validateAccessToken(accessToken, commentDto.getUserId());

        int result = commentService.save(commentDto);

        if (result > 0) {
            return ResponseEntity.ok("댓글 작성 성공");
        } else {
            throw new IllegalStateException("댓글 작성 실패");
        }
    }

    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 모든 댓글을 조회합니다.")
    @GetMapping("/list/{boardId}")
    public ResponseEntity<List<CommentDto>> list(@PathVariable("boardId") int boardId) {
        List<CommentDto> commentList = commentService.findAllByBoardId(boardId);
        return ResponseEntity.ok(commentList);
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @PatchMapping("/modify")
    public ResponseEntity<String> modify(
            @RequestBody CommentDto commentDto,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String accessToken) {

        jwtUtil.validateAccessToken(accessToken, commentDto.getUserId());

        int result = commentService.modify(commentDto);

        if (result > 0) {
            return ResponseEntity.ok("댓글 수정 성공");
        } else {
            throw new IllegalStateException("댓글 수정 실패");
        }
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(
            @PathVariable int id,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String accessToken) {

        CommentDto commentDto = commentService.findById(id);

        jwtUtil.validateAccessToken(accessToken, commentDto.getUserId());

        int result = commentService.delete(id);

        if (result > 0) {
            return ResponseEntity.ok("댓글 삭제 성공");
        } else {
            throw new IllegalStateException("댓글 삭제 실패");
        }
    }
}
