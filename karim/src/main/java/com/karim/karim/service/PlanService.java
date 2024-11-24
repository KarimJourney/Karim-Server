package com.karim.karim.service;

import com.karim.karim.dto.PlanDto;
import com.karim.karim.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlanService {

    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public PlanDto findById(int id) {
        return planRepository.findById(id);
    }

    public List<PlanDto> findByUserId(long userId) {
        return planRepository.findByUserId(userId);
    }

    public int savePlan(PlanDto planDto) {
        return planRepository.save(planDto);
    }

    public int modifyPlan(PlanDto planDto) {
        return planRepository.modify(planDto);
    }

    public int deletePlan(int id) {
        return planRepository.delete(id);
    }
}
