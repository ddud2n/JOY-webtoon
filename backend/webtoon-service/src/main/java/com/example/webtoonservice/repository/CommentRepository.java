package com.example.webtoonservice.repository;

import com.example.webtoonservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value="Select * from comment c where c.epi_id = ?1", nativeQuery = true)
    Collection<Comment> getComment(Integer id); // 에피소드id -> 관련 댓글 전체 조회
}