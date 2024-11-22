package com.karim.karim.repository;

import com.karim.karim.dto.FileDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FileRepository {
    public int save(@Param("board_id") int board_id, @Param("fileDto") FileDto fileDto);
}
