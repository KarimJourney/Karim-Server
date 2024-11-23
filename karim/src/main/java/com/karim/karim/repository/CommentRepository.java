package com.karim.karim.repository;

import com.karim.karim.dto.CommentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface CommentRepository {
    int save(CommentDto commentDto);
    List<CommentDto> findAllByBoardId(int boardId);
    CommentDto findById(int id);
    int modify(CommentDto commentDto);
    int delete(int id);
}
