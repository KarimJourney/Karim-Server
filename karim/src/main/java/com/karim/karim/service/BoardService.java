package com.karim.karim.service;

import com.karim.karim.dto.BoardDto;
import com.karim.karim.dto.FileDto;
import com.karim.karim.repository.BoardRepository;
import com.karim.karim.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final FileRepository fileRepository;

    @Value("${file.path.upload-images}")
    private String uploadPath;

    public BoardService(BoardRepository boardRepository, FileRepository fileRepository) {
        this.boardRepository = boardRepository;
        this.fileRepository = fileRepository;
    }

    @Transactional
    public int save(BoardDto boardDto, MultipartFile[] files) throws Exception {
        int id = boardRepository.save(boardDto); // 결과값 리턴 X, 방금 만든 게시글의 ID를 리턴하여 파일 저장에 활용

        if (!files[0].isEmpty()) {
            String today = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = uploadPath + File.separator + today;
            File folder = new File(saveFolder);
            if (!folder.exists()) folder.mkdirs();

            for (MultipartFile mfile : files) {
                String originalFileName = mfile.getOriginalFilename();
                if (!originalFileName.isEmpty()) {
                    String saveFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf('.'));
                    FileDto fileDto = new FileDto(today, originalFileName, saveFileName);
                    mfile.transferTo(new File(folder, saveFileName));
                    fileRepository.save(id, fileDto); // 실제 file table에는 board_id가 들어가있음 (참조용)
                }
            }
            return 1;
        }
        return 0;
    }
    public List<BoardDto> findAll() { return boardRepository.findAll(); }
    public BoardDto findByBoardId(int id) { return boardRepository.findByBoardId(id); }
    public int modify(BoardDto boardDto) { return boardRepository.modify(boardDto); }
    public int modifyHit(int id) { return boardRepository.modifyHit(id); }
    public int delete(int id) { return boardRepository.delete(id); }
}
