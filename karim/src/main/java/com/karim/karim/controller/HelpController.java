package com.karim.karim.controller;

import com.karim.karim.dto.HelpDto;
import com.karim.karim.service.HelpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/help")
@Tag(name = "Help Controller", description = "QnA 게시판 관련 API")
public class HelpController {

    private final HelpService helpService;

    public HelpController(HelpService helpService) {
        this.helpService = helpService;
    }

    @Operation(
            summary = "QnA 전체 조회",
            description = "모든 QnA 데이터를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = HelpDto.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping()
    public ResponseEntity<List<HelpDto>> findAll() {
        List<HelpDto> helpList = helpService.findAll();
        return ResponseEntity.ok(helpList);
    }
}

