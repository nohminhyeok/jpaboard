package com.example.jpaboard.dto;

import com.example.jpaboard.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardForm {
	private int boardNo;
	private String boardTitle;
	private String boardContent;
	
	public Board toEntity() {
		Board entity = new Board();
		entity.setBoardNo(this.boardNo);
		entity.setBoardTitle(this.boardTitle);
		entity.setBoardContent(this.boardContent);
		return entity;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
