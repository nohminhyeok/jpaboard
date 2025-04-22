package com.example.jpaboard.util;

import java.security.MessageDigest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SHA256Util {
	// 문자열을 입력하면 SHA256 암호화된 16진수 문자열을 반환
	public static String encoding(String src) {
		String dest = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(src.getBytes());
			// 암호화된 바이트를 문자로(16진수 2자리)변경
			StringBuffer sb = new StringBuffer();
			for(byte b : md.digest()) {
				sb.append(String.format("%02x", b));
			}
			dest = sb.toString();
		}catch(Exception e) {
			e.printStackTrace();
			log.debug("PW 암호화 실패");
		}
		
		return dest;
	}
}
