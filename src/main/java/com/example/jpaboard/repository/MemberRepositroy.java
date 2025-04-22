package com.example.jpaboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpaboard.entity.Member;
import com.example.jpaboard.entity.MemberOnlyMemberId;

public interface MemberRepositroy extends JpaRepository<Member, Integer>{
	// member_id 중복 검사
	boolean existsByMemberId(String memberId);
	// 로그인 하는 추상메서드 : findBy 엔티티 컬럼 필드 ... And 엔티티 컬럼 필드
	MemberOnlyMemberId findByMemberIdAndMemberPw(String memberId, String memberPw);
	Page<Member> findByMemberIdContainingAndMemberRoleContaining(String memberId, String memberRole, Pageable pageable);
	Member findByMemberId(String memberId);
	
}
