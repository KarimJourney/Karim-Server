package com.karim.karim.repository;

import com.karim.karim.dto.PlaceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

@Mapper
public interface PlaceRepository {
    List<PlaceDto> findByPlanId(int planId);
    int countPlaces(PlaceDto placeDto);
    int save(PlaceDto placeDto);
    int modify(PlaceDto placeDto);
    int delete(PlaceDto placeDto);
}
