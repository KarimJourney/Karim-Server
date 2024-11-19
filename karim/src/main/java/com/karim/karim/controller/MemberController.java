package com.karim.karim.controller;

import com.karim.karim.domain.Member;
import com.karim.karim.service.MemberService;
import com.karim.karim.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
@Tag(name = "Member Controller", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    public MemberController(MemberService memberService, JWTUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(
            summary = "회원 조회",
            description = "회원 ID를 통해 회원 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 조회 성공",
                            content = @Content(schema = @Schema(implementation = Member.class))),
                    @ApiResponse(responseCode = "404", description = "회원 조회 실패")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Member> findById(@PathVariable String id) {
        Member member = memberService.findById(id);
        if (member == null) {
            throw new IllegalStateException("회원 조회 실패: 해당 회원을 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(member);
    }

    @Operation(summary = "전체 회원 조회", description = "모든 회원 정보를 조회합니다.")
    @GetMapping("/all")
    public ResponseEntity<List<Member>> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    @Operation(summary = "회원 가입", description = "회원 정보를 입력받아 새로운 회원을 등록합니다.")
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody Member member) {
        memberService.join(member);
        return ResponseEntity.ok("회원 가입 성공");
    }

    @Operation(
            summary = "로그인",
            description = "회원 ID와 비밀번호로 로그인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공",
                            content = @Content(schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "401", description = "로그인 실패")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String id, @RequestParam String password) {
        Member member = memberService.login(id, password);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", jwtUtil.createAccessToken(member.getId()));
        response.put("refreshToken", jwtUtil.createRefreshToken(member.getId()));
        response.put("member", member);
        response.put("message", "로그인 성공");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PatchMapping("/modify")
    public ResponseEntity<String> modify(@RequestBody Member member) {
        int result = memberService.modify(member);
        if (result <= 0) {
            throw new IllegalStateException("회원 정보 수정 실패");
        }
        return ResponseEntity.ok("회원 정보 수정 성공");
    }

    @Operation(summary = "회원 탈퇴", description = "회원 ID를 통해 회원 탈퇴를 진행합니다.")
    @DeleteMapping("/withdraw/{id}")
    public ResponseEntity<String> withdraw(@PathVariable String id) {
        int result = memberService.withdraw(id);
        if (result <= 0) {
            throw new IllegalStateException("회원 탈퇴 실패: 해당 회원을 찾을 수 없습니다.");
        }
        return ResponseEntity.ok("회원 탈퇴 성공");
    }

    @Operation(
            summary = "토큰 재발급",
            description = "리프레시 토큰을 통해 새로운 액세스 토큰을 발급합니다."
    )
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String refreshToken) {

        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new IllegalStateException("유효하지 않은 Authorization 헤더입니다.");
        }

        String id = jwtUtil.getId(refreshToken);

        if (id == null) {
            throw new IllegalStateException("유효하지 않은 리프레시 토큰입니다.");
        }

        String newAccessToken = jwtUtil.createAccessToken(id);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("message", "토큰 재발급 완료");

        return ResponseEntity.ok(response);
    }
}
