package com.karim.karim.repository;

import com.karim.karim.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberRepository {
    MemberDto findById(Long id);
    List<MemberDto> findAll();
    int join(MemberDto memberDto);
    int modify(MemberDto memberDto);
    int withdraw(Long id);
}
