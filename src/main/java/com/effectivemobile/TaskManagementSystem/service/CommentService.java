package com.effectivemobile.TaskManagementSystem.service;

import com.effectivemobile.TaskManagementSystem.exception.notFound.CommentNotFoundException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.TaskNotFoundException;
import com.effectivemobile.TaskManagementSystem.model.Comment;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CommentService {

    @Autowired
    public CommentRepository commentRepository;

    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("Comment with id [" + id + "] not found!"));
    }

    public Page<Comment> findCommentsByTask(Task task, Pageable pageable) {
        return commentRepository.findAllCommentsByTask(task, pageable);
    }

    public void addNewComment(Comment comment) {
        commentRepository.save(comment);
    }
}
