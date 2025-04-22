package com.example.jpaboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpaboard.entity.Member;

public interface MemberRepositroy extends JpaRepository<Member, Integer>{
	// member_id 중복 검사
	// 로그인 하는 추상메서드
	boolean existsByMemberId(String mmeberId);
}
