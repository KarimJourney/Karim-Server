package com.karim.karim.repository;

import com.karim.karim.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardRepository {
    public int save(BoardDto boardDto);
    public List<BoardDto> findAll();
    public BoardDto findByBoardId(int id);
    public int modify(BoardDto boardDto);
    public int modifyHit(int id);
    public int delete(int id);
}
