package com.karim.karim.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/weather")
@Tag(name = "Weather Controller", description = "기상청 날씨 요청 API")
public class WeatherController {

    @Value("${weather.api.base-url}")
    private String weatherApiBaseUrl;

    @Value("${weather.api.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    private static final double RE = 6371.00877; // 지구 반경 (km)
    private static final double GRID = 5.0; // 격자 간격 (km)
    private static final double SLAT1 = 30.0; // 투영 위도 1 (degree)
    private static final double SLAT2 = 60.0; // 투영 위도 2 (degree)
    private static final double OLON = 126.0; // 기준점 경도 (degree)
    private static final double OLAT = 38.0; // 기준점 위도 (degree)
    private static final double XO = 43; // 기준점 X 좌표
    private static final double YO = 136; // 기준점 Y 좌표

    public static int[] convertToGrid(double lat, double lon) {
        double DEGRAD = Math.PI / 180.0;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        double ra = Math.tan(Math.PI * 0.25 + lat * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = lon * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;

        int x = (int) Math.floor(ra * Math.sin(theta) + XO + 0.5);
        int y = (int) Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

        return new int[]{x, y};
    }

    public WeatherController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Operation(summary = "초단기실황 조회", description = "1시간 단위로 실시간 날씨를 조회합니다.")
    @GetMapping()
    public ResponseEntity<Object> getWeather(
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            @RequestParam("nx") double nx,
            @RequestParam("ny") double ny
    ) {
        try {
            int[] n = convertToGrid(nx, ny);

            // API 요청 URL 구성
            String apiUrl = String.format(
                    "%s?serviceKey=%s&pageNo=1&numOfRows=1000&dataType=JSON&base_date=%s&base_time=%s&nx=%d&ny=%d",
                    weatherApiBaseUrl, serviceKey, date, time, n[0], n[1]
            );

            System.out.println(apiUrl);
            // API 호출
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            // 에러 처리
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Weather API 호출 중 문제가 발생했습니다.");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}