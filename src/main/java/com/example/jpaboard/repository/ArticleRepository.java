package com.example.jpaboard.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.jpaboard.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

	Page<Article> findByTitleContaining(String word, Pageable pageable);

	@Query(nativeQuery = true,
	       value = "SELECT MIN(id) AS minId, MAX(id) AS maxId, COUNT(*) AS cnt " +
	               "FROM article " +
	               "WHERE title LIKE :word")
	Map<String, Object> getMinMaxCount(String word);
}
