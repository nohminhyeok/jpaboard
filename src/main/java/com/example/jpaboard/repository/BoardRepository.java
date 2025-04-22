package com.example.jpaboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jpaboard.entity.Article;
import com.example.jpaboard.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    Page<Board> findByBoardTitleContaining(String word, Pageable pageable); // ← 필드명 맞춤
}
