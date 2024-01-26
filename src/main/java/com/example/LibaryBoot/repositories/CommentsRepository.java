package com.example.LibaryBoot.repositories;

import com.example.LibaryBoot.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findCommentByBookId(int bookId);
}
