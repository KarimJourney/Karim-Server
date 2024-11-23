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
        // 게시글 저장
        int result = boardRepository.save(boardDto);
        int boardId = boardDto.getId(); // 저장된 게시글의 ID 가져오기

        // 파일이 있는 경우 파일 저장 처리
        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            String today = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = uploadPath + File.separator + today;
            File folder = new File(saveFolder);
            if (!folder.exists()) folder.mkdirs();

            for (MultipartFile mfile : files) {
                String originalFileName = mfile.getOriginalFilename();
                if (!originalFileName.isEmpty()) {
                    String saveFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf('.'));
                    FileDto fileDto = new FileDto(boardId, today, originalFileName, saveFileName);
                    mfile.transferTo(new File(folder, saveFileName));
                    fileRepository.save(fileDto); // 파일 정보 저장
                }
            }
        }
        return result;
    }

    @Transactional
    public int modify(BoardDto boardDto, MultipartFile[] files) throws Exception {
        // 게시글 수정
        int result = boardRepository.modify(boardDto);
        int boardId = boardDto.getId();

        // 기존 파일 삭제 로직 (필요한 경우 추가)
        // 예를 들어, 기존 파일들을 모두 삭제하고 새로운 파일로 교체한다면:
        fileRepository.deleteByBoardId(boardId);

        // 새로운 파일 저장
        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            String today = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = uploadPath + File.separator + today;
            File folder = new File(saveFolder);
            if (!folder.exists()) folder.mkdirs();

            for (MultipartFile mfile : files) {
                String originalFileName = mfile.getOriginalFilename();
                if (!originalFileName.isEmpty()) {
                    String saveFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf('.'));
                    FileDto fileDto = new FileDto(boardId, today, originalFileName, saveFileName);
                    mfile.transferTo(new File(folder, saveFileName));
                    fileRepository.save(fileDto); // 파일 정보 저장
                }
            }
        }
        return result;
    }

    public List<BoardDto> findAll() {
        return boardRepository.findAll();
    }

    public BoardDto findByBoardId(int id) {
        BoardDto boardDto = boardRepository.findByBoardId(id);
        if (boardDto != null) {
            List<FileDto> files = fileRepository.findByBoardId(id);
            boardDto.setFiles(files);
        }
        return boardDto;
    }

    public int modifyHit(int id) {
        return boardRepository.modifyHit(id);
    }

    public int delete(int id) {
        // 게시글 삭제 전에 해당 게시글의 파일들을 삭제
        fileRepository.deleteByBoardId(id);
        return boardRepository.delete(id);
    }
}
