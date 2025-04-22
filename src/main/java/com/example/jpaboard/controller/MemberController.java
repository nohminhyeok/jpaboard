package com.example.jpaboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jpaboard.dto.MemberForm;
import com.example.jpaboard.entity.Member;
import com.example.jpaboard.entity.MemberOnlyMemberId;
import com.example.jpaboard.repository.MemberRepositroy;
import com.example.jpaboard.util.SHA256Util;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MemberController {
    @Autowired
    MemberRepositroy memberRepository;

    // 회원가입 폼
    @GetMapping("/member/joinMember")
    public String joinMember() {
        return "member/joinMember";
    }

    // 회원가입 처리
    @PostMapping("/member/joinMember")
    public String joinMember(MemberForm memberForm, RedirectAttributes rda) {
        log.debug(memberForm.toString());

        if (memberRepository.existsByMemberId(memberForm.getMemberId())) {
            rda.addFlashAttribute("msg", memberForm.getMemberId() + " ID가 이미 존재합니다.");
            return "redirect:/member/joinMember";
        }

        memberForm.setMemberPw(SHA256Util.encoding(memberForm.getMemberPw()));
        Member member = memberForm.toEntity();
        memberRepository.save(member);
        return "redirect:/member/login";
    }

    // 로그인 폼
    @GetMapping("/")
    public String login() {
        return "member/login";
    }

    // 로그인 처리
    @PostMapping("/member/login")
    public String login(HttpSession session, MemberForm memberForm, RedirectAttributes rda) {
        memberForm.setMemberPw(SHA256Util.encoding(memberForm.getMemberPw()));
        MemberOnlyMemberId loginMember = memberRepository.findByMemberIdAndMemberPw(memberForm.getMemberId(), memberForm.getMemberPw());

        if (loginMember == null) {
            rda.addFlashAttribute("msg", "로그인 실패");
            return "redirect:/member/login";
        }

        session.setAttribute("loginMember", loginMember);
        return "redirect:/home";
    }

    // 로그아웃
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 회원 목록
    @GetMapping("/member/memberList")
    public String memberList(HttpSession session,
                             Model model,
                             @RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                             @RequestParam(value = "rowPerPage", defaultValue = "10") int rowPerPage,
                             @RequestParam(value = "memberId", defaultValue = "") String memberId,
                             @RequestParam(value = "memberRole", defaultValue = "") String memberRole) {

        if (session.getAttribute("loginMember") == null) {
            return "redirect:/member/login";
        }

        Sort sort = Sort.by("memberNo").ascending();
        PageRequest pageable = PageRequest.of(currentPage, rowPerPage, sort);
        Page<Member> list = memberRepository.findByMemberIdContainingAndMemberRoleContaining(memberId, memberRole, pageable);

        model.addAttribute("memberId", memberId);
        model.addAttribute("memberRole", memberRole);
        model.addAttribute("memberList", list);
        model.addAttribute("currentPage", list.getNumber());
        model.addAttribute("hasNext", list.hasNext());
        model.addAttribute("isFirst", list.isFirst());
        model.addAttribute("nextPage", list.getNumber() + 1);
        model.addAttribute("prePage", list.getNumber() - 1);

        return "member/memberList";
    }

    // 비밀번호 수정 폼
    @GetMapping("/member/modifyMemberPw")
    public String modifyMemberPw(HttpSession session, Model model) {
        MemberOnlyMemberId loginMember = (MemberOnlyMemberId) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/member/login";
        }

        Member member = memberRepository.findByMemberId(loginMember.getMemberId());
        model.addAttribute("member", member);
        return "member/modifyMemberPw";
    }

    // 비밀번호 수정 처리
    @PostMapping("/member/modifyMemberPw")
    public String updateMemberPw(@RequestParam String newPw,
                                 HttpSession session,
                                 RedirectAttributes rda) {
        MemberOnlyMemberId loginMember = (MemberOnlyMemberId) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/member/login";
        }

        Member member = memberRepository.findByMemberId(loginMember.getMemberId());
        member.setMemberPw(SHA256Util.encoding(newPw));
        memberRepository.save(member);

        rda.addFlashAttribute("msg", "비밀번호가 수정되었습니다.");
        return "redirect:/";
    }

    // 탈퇴 폼
    @GetMapping("/member/removeMember")
    public String deleteMember(HttpSession session, Model model) {
        MemberOnlyMemberId loginMember = (MemberOnlyMemberId) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/member/login";
        }

        return "member/removeMember";
    }

    // 탈퇴 처리
    @PostMapping("/member/deleteMember")
    public String deleteMemberAction(HttpSession session,
                                     @RequestParam String memberPw,
                                     RedirectAttributes rda) {
        MemberOnlyMemberId loginMember = (MemberOnlyMemberId) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/member/login";
        }

        String encodedPw = SHA256Util.encoding(memberPw);
        Member member = memberRepository.findByMemberId(loginMember.getMemberId());

        if (member == null || !member.getMemberPw().equals(encodedPw)) {
            rda.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
            return "redirect:/member/removeMember";
        }

        memberRepository.delete(member);
        session.invalidate();
        rda.addFlashAttribute("msg", "회원 탈퇴가 완료되었습니다.");

        return "redirect:/";
    }
}
