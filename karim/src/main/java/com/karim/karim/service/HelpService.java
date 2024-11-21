package com.karim.karim.service;

import com.karim.karim.dto.HelpDto;
import com.karim.karim.repository.HelpRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HelpService {

    private final HelpRepository helpRepository;

    public HelpService(HelpRepository helpRepository) {
        this.helpRepository = helpRepository;
    }

    public List<HelpDto> findAll() {
        return helpRepository.findAll();
    }
}
