package com.example.jpaboard.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jpaboard.dto.MemberForm;
import com.example.jpaboard.entity.Member;
import com.example.jpaboard.repository.MemberRepositroy;
import com.example.jpaboard.util.SHA256Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MemberController {
	@Autowired
	MemberRepositroy memberRepository;
	
	// 회원가입 + member_id 중복확인
	@GetMapping("/member/joinMember")
	public String joinMember() {
		return "member/joinMember";
	}
	
	@PostMapping("/member/joinMember")
	public String joinMember(MemberForm memberForm, RedirectAttributes rda) {
		
		log.debug(memberForm.toString());
		log.debug("isMemberId : "+memberRepository.existsByMemberId(memberForm.getMemberId()));
		
		if(memberRepository.existsByMemberId(memberForm.getMemberId())) {
			rda.addFlashAttribute("msg", memberForm.getMemberId()+"ID가 이미 존재합니다.");
			return "redirect:/member/joinMember";
		}
		
		// 회원가입 진행
		// memberForm.getMemberPw() 값을 SHA-256 방식으로 암호화
		memberForm.setMemberPw(SHA256Util.encoding(memberForm.getMemberPw()));
		
		Member member = memberForm.toEntity();
		memberRepository.save(member); // entity저장 -> 최종 커밋시 -> 테이블에 행이 추가
		
		
		return "redirect:/member/login";
	}
	
	// 로그인
	@GetMapping("/member/login")
	public String login() {
		return "member/login";
	}
	
	// 로그아웃
	
	// 회원정보 수정
	
	// 회원목록
	
	// 회원탈퇴
}
