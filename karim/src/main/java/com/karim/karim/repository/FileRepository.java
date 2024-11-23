package com.karim.karim.repository;

import com.karim.karim.dto.FileDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileRepository {
    int save(FileDto fileDto);
    int deleteByBoardId(int boardId);
    List<FileDto> findByBoardId(int boardId); // 파일 정보를 가져오는 메서드 (파일 삭제 시 필요)
}
