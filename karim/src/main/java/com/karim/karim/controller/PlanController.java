package com.karim.karim.controller;

import com.karim.karim.dto.PlaceDto;
import com.karim.karim.dto.PlaceListDto;
import com.karim.karim.dto.PlanDto;
import com.karim.karim.service.PlaceService;
import com.karim.karim.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/plan")
@Tag(name = "Plan Controller", description = "여행 계획 관련 API")
public class PlanController {

    private final PlanService planService;
    private final PlaceService placeService;

    public PlanController(PlanService planService, PlaceService placeService) {
        this.planService = planService;
        this.placeService = placeService;
    }

    @Operation(summary = "전체 계획 조회", description = "사용자가 등록한 모든 여행 계획을 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<List<PlanDto>> findByUserId(@PathVariable("userId") Long userId) {
        List<PlanDto> plans = planService.findByUserId(userId);
        return ResponseEntity.ok(plans);
    }

    @Operation(summary = "계획 생성", description = "새로운 여행 계획을 등록합니다.")
    @PostMapping()
    public ResponseEntity<String> save(@RequestBody PlanDto planDto) {
        planService.savePlan(planDto);
        return ResponseEntity.ok("계획 생성 성공");
    }

    @Operation(summary = "계획 수정", description = "기존 여행 계획을 수정합니다.")
    @PatchMapping()
    public ResponseEntity<String> modify(@RequestBody PlanDto planDto) {
        int result = planService.modifyPlan(planDto);
        if (result > 0) {
            return ResponseEntity.ok("계획 수정 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("계획 수정 실패");
        }
    }

    @Operation(summary = "계획 삭제", description = "특정 ID의 여행 계획을 삭제합니다.")
    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<String> delete(@PathVariable int id, @PathVariable Long userId) {
        int result = planService.deletePlan(id, userId);
        if (result > 0) {
            return ResponseEntity.ok("계획 삭제 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("계획 삭제 실패");
        }
    }

    @Operation(summary = "계획 상세 조회", description = "특정 여행 계획과 해당 계획의 장소 목록을 조회합니다.")
    @GetMapping("/{userId}/{id}")
    public ResponseEntity<Map<String, Object>> getPlanDetails(@PathVariable int id, @PathVariable Long userId) {
        PlanDto plan = planService.findByIdAndUserId(id, userId);
        List<PlaceDto> places = placeService.findByPlanId(id);

        Map<String, Object> response = new HashMap<>();
        response.put("plan", plan);
        response.put("places", places);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "계획 내 장소 추가", description = "특정 계획에 새로운 장소를 추가합니다.")
    @PostMapping("/place")
    public ResponseEntity<String> addPlaceToPlan(@PathVariable int id, @RequestBody PlaceDto placeDto) {
        placeDto.setPlanId(id);
        int result = placeService.save(placeDto);
        if (result > 0) {
            return ResponseEntity.ok("장소 추가 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("장소 추가 실패");
        }
    }

    @Operation(summary = "계획 내 장소 수정", description = "특정 계획 내 장소를 수정합니다.")
    @PatchMapping("/place")
    public ResponseEntity<String> modifyPlaceInPlan(@PathVariable int id, @RequestBody PlaceListDto placeListDto) {
        int result = placeService.modify(placeListDto.getData());
        if (result > 0) {
            return ResponseEntity.ok("장소 수정 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("장소 수정 실패");
        }
    }

    @Operation(summary = "계획 내 장소 삭제", description = "특정 계획에서 장소를 삭제합니다.")
    @DeleteMapping("/{id}/{placeId}")
    public ResponseEntity<String> deletePlaceFromPlan(@PathVariable int id, @PathVariable int placeId) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setId(placeId);
        placeDto.setPlanId(id);

        int result = placeService.delete(placeDto);
        if (result > 0) {
            return ResponseEntity.ok("장소 삭제 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("장소 삭제 실패");
        }
    }
}