package com.karim.karim.controller;

import com.karim.karim.dto.KakaoUserInfoResponseDto;
import com.karim.karim.dto.MemberDto;
import com.karim.karim.service.KakaoService;
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

import java.util.*;

@RestController
@RequestMapping("/member")
@Tag(name = "Member Controller", description = "회원 관련 API")
public class MemberController {

    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    public MemberController(KakaoService kakaoService, MemberService memberService, JWTUtil jwtUtil) {
        this.kakaoService = kakaoService;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "카카오 로그인 / 회원가입", description = "카카오 인증 후 로그인 또는 회원가입을 처리합니다.")
    @GetMapping("/kakaologin/{code}")
    public ResponseEntity<?> kakaoLogin(@PathVariable("code") String code) {

        String kakaoAccessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(kakaoAccessToken);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getKakaoAccount().getProfile().getNickName();

        MemberDto member = memberService.findById(kakaoId);

        String newAccessToken = jwtUtil.createAccessToken(String.valueOf(kakaoId));
        String refreshToken = jwtUtil.createRefreshToken(String.valueOf(kakaoId));

        if (member != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            response.put("refreshToken", refreshToken);
            response.put("member", member);
            response.put("message", "로그인");

            return ResponseEntity.ok(response);
        } else {
            MemberDto memberDto = new MemberDto(kakaoId, nickname);
            memberService.join(memberDto);
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            response.put("refreshToken", refreshToken);
            response.put("member", memberDto);
            response.put("message", "회원가입");

            return ResponseEntity.ok(response);
        }
    }

    @Operation(
            summary = "회원 조회",
            description = "회원 ID를 통해 회원 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 조회 성공",
                            content = @Content(schema = @Schema(implementation = MemberDto.class))),
                    @ApiResponse(responseCode = "404", description = "회원 조회 실패")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> findById(@PathVariable Long id) {

        MemberDto memberDto = memberService.findById(id);
        if (memberDto == null) {
            throw new IllegalStateException("회원 조회 실패: 해당 회원을 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(memberDto);
    }

    @Operation(summary = "전체 회원 조회", description = "모든 회원 정보를 조회합니다.")
    @GetMapping("/all")
    public ResponseEntity<List<MemberDto>> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정을 진행합니다.")
    @PatchMapping("/modify")
    public ResponseEntity<String> modify(
            @RequestBody MemberDto memberDto,
            @RequestHeader(value = "Authorization", required = false) String accessToken) {

        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new IllegalStateException("AccessToken이 제공되지 않았거나 유효하지 않습니다.");
        }

        String tokenId = jwtUtil.getId(accessToken);

        if (!String.valueOf(memberDto.getId()).equals(tokenId)) {
            throw new IllegalStateException("AccessToken의 사용자와 수정 요청된 ID가 일치하지 않습니다.");
        }

        int result = memberService.modify(memberDto);

        if (result > 0) {
            return ResponseEntity.ok("회원 정보 수정 성공");
        } else {
            throw new IllegalStateException("회원 정보 수정 실패");
        }
    }

    @Operation(summary = "회원 탈퇴", description = "회원 ID를 통해 회원 탈퇴를 진행합니다.")
    @DeleteMapping("/withdraw/{id}")
    public ResponseEntity<String> withdraw(
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String accessToken) {

        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new IllegalStateException("AccessToken이 제공되지 않았거나 유효하지 않습니다.");
        }

        String tokenId = jwtUtil.getId(accessToken);

        if (!String.valueOf(id).equals(tokenId)) {
            throw new IllegalStateException("AccessToken의 사용자와 요청된 ID가 일치하지 않습니다.");
        }

        int result = memberService.withdraw(id);

        if (result > 0) {
            return ResponseEntity.ok("회원 탈퇴 성공");
        } else {
            throw new IllegalStateException("회원 탈퇴 실패: 해당 회원을 찾을 수 없습니다.");
        }
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
