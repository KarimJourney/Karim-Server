package com.karim.karim.repository;

import com.karim.karim.dto.HelpDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface HelpRepository {
    List<HelpDto> findAll();
}
