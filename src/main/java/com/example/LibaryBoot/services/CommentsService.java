package com.example.LibaryBoot.services;

import com.example.LibaryBoot.models.Comment;
import com.example.LibaryBoot.repositories.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService {
    private final CommentsRepository commentsRepository;
    @Autowired
    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }
    public List<Comment> getAll(){
        return commentsRepository.findAll();
    }
    public List<Comment> findCommentsByBookId(int bookId){
        return commentsRepository.findCommentByBookId(bookId);
    }
    public void save(Comment comment){
        commentsRepository.save(comment);
    }
}
