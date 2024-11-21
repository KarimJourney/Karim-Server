package com.karim.karim.service;

import com.karim.karim.dto.MemberDto;
import com.karim.karim.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDto findById(Long id) {
        return memberRepository.findById(id);
    }

    public List<MemberDto> findAll() {
        return  memberRepository.findAll();
    }

    public int join(MemberDto memberDto) {
        return memberRepository.join(memberDto);
    }

    public int modify(MemberDto memberDto){
        return memberRepository.modify(memberDto);
    }

    public int withdraw(Long id) {
        return memberRepository.withdraw(id);
    }
}
