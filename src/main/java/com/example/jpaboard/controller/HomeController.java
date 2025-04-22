package com.example.jpaboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	@GetMapping("/Home")
	public String home() {
		// model.addAttribute("loginName", "구디");
		// System.out.println(model.getAttribute("loginName"));
		// log 프레임워크 사용
		// log.debug("loginName: "+model.getAttribute("loginName")); // 변수값
		// log.info("loginName: "+model.getAttribute("loginName")); // 기타 로그인 성공 같은 거
		return "home";
	}
}
