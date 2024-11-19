package com.karim.karim.service;

import com.karim.karim.domain.Member;
import com.karim.karim.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findById(String id) {
        return memberRepository.findById(id);
    }

    public List<Member> findAll() {
        return  memberRepository.findAll();
    }

    public int join(Member member) {
        Member existingMember = memberRepository.findById(member.getId());
        if (existingMember != null) throw new IllegalStateException("이미 존재하는 회원입니다.");
        return memberRepository.join(member);
    }

    public Member login(String id, String password) {
        Member member = findById(id);
        if (member != null && password.equals(member.getPassword())) return member;
        throw new IllegalStateException("로그인에 실패하셨습니다.");
    }

    public int modify(Member member){
        if (member.getPassword() != null && !member.getPassword().isEmpty()) return memberRepository.modifyWithPw(member);
        else return memberRepository.modifyWithoutPw(member);
    }

    public int withdraw(String id) {
        return memberRepository.withdraw(id);
    }
}
