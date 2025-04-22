package com.example.jpaboard.dto;

import com.example.jpaboard.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardForm {
	private int board_no;
	private String board_title;
	private String board_content;
	
	public Board toEntity() {
		Board entity = new Board();
		entity.setBoardNo(this.board_no);
		entity.setBoardTitle(this.board_title);
		entity.setBoardContent(this.board_title);
		return entity;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
