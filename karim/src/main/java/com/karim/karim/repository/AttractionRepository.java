package com.karim.karim.repository;

import com.karim.karim.dto.AttractionDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttractionRepository {
    int save(AttractionDto attractionDto);
}