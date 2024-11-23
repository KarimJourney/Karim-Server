package com.karim.karim.service;

import com.karim.karim.dto.CommentDto;
import com.karim.karim.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public int save(CommentDto commentDto) {
        return commentRepository.save(commentDto);
    }

    public List<CommentDto> findAllByBoardId(int boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }

    public CommentDto findById(int id) {
        return commentRepository.findById(id);
    }

    public int modify(CommentDto commentDto) {
        return commentRepository.modify(commentDto);
    }

    public int delete(int id) {
        return commentRepository.delete(id);
    }
}
