package com.karim.karim.repository;

import com.karim.karim.dto.PlanDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

@Mapper
public interface PlanRepository {
    PlanDto findById(int id);
    List<PlanDto> findByUserId(long userId);
    int save(PlanDto planDto);
    int modify(PlanDto planDto);
    int delete(int id);
}
