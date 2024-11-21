package com.karim.karim.service;

import com.karim.karim.dto.AttractionDto;
import com.karim.karim.dto.PlaceDto;
import com.karim.karim.repository.AttractionRepository;
import com.karim.karim.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PlaceService {

    private final AttractionRepository attractionRepository;
    private final PlaceRepository placeRepository;

    public PlaceService(AttractionRepository attractionRepository, PlaceRepository placeRepository) {
        this.attractionRepository = attractionRepository;
        this.placeRepository = placeRepository;
    }

    public List<PlaceDto> findByPlanId(int planId) {
        return placeRepository.findByPlanId(planId);
    }

    @Transactional
    public int save(PlaceDto placeDto) {
        attractionRepository.save(new AttractionDto(placeDto.getAttrId(), placeDto.getName(), placeDto.getAddress(), placeDto.getLatitude(), placeDto.getLongitude()));
        int count = placeRepository.countPlaces(placeDto);
        placeDto.setOrder(count + 1);
        return placeRepository.save(placeDto);
    }

    @Transactional
    public int modify(List<PlaceDto> places) {
        for (PlaceDto place : places) {
            int result = placeRepository.modify(place);
            if (result == 0) {
                return 0;
            }
        }
        return 1;
    }

    public int delete(PlaceDto placeDto) {
        return placeRepository.delete(placeDto);
    }
}
