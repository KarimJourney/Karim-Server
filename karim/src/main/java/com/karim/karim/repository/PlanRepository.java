package com.karim.karim.repository;

import com.karim.karim.dto.PlanDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface PlanRepository {
    PlanDto findByIdAndUserId(int id, long userId);
    List<PlanDto> findByUserId(long userId);
    int save(PlanDto planDto);
    int modify(PlanDto planDto);
    int delete(int id, long userId);
}
