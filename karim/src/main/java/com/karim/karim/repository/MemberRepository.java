package com.karim.karim.repository;

import com.karim.karim.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberRepository {
    Member findById(String id);
    List<Member> findAll();
    int join(Member member);
    int modifyWithPw(Member member);
    int modifyWithoutPw(Member member);
    int withdraw(String id);
}
