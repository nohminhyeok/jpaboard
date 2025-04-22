package com.example.jpaboard.controller;

import java.util.List;

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

import com.example.jpaboard.dto.BoardForm;
import com.example.jpaboard.entity.Board;
import com.example.jpaboard.repository.BoardRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardController {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // ✅ 루트 URL 접속 시 게시판 목록으로 리다이렉트
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/board/boardList";
    }

    @GetMapping("/board/addBoard")
    public String newBoardForm() {
        return "board/addBoard";
    }

    @PostMapping("/board/addBoard")
    public String addBoard(BoardForm form) {
        Board entity = form.toEntity();
        boardRepository.save(entity);
        return "redirect:/board/boardList";
    }

    @GetMapping("/board/boardList")
    public String boardList(Model model,
                            @RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                            @RequestParam(value = "rowPerPage", defaultValue = "10") int rowPerPage,
                            @RequestParam(value = "word", defaultValue = "") String word) {

        Sort sort = Sort.by("boardTitle").ascending();
        PageRequest pageable = PageRequest.of(currentPage, rowPerPage, sort);

        Page<Board> list = boardRepository.findByBoardTitleContaining(word, pageable);

        log.debug("list.getTotalElements: " + list.getTotalElements());
        log.debug("list.getTotalPages: " + list.getTotalPages());
        log.debug("list.getNumber: " + list.getNumber());
        log.debug("list.getSize: " + list.getSize());
        log.debug("list.isFirst: " + list.isFirst());
        log.debug("list.hasNext: " + list.hasNext());

        model.addAttribute("boardList", list);
        model.addAttribute("nextPage", list.getNumber() + 1);
        model.addAttribute("prePage", list.getNumber() - 1);
        model.addAttribute("word", word);

        return "board/boardList";
    }

    @GetMapping("/board/boardOne")
    public String boardOne(Model model, @RequestParam int id) {
        Board board = boardRepository.findById(id).orElse(null);
        model.addAttribute("board", board);
        return "board/boardOne";
    }

    @GetMapping("/board/modifyBoard")
    public String modifyBoard(Model model, @RequestParam int id) {
        Board board = boardRepository.findById(id).orElse(null);
        model.addAttribute("board", board);
        return "board/modifyBoard";
    }

    @PostMapping("/board/modifyBoard")
    public String updateBoard(BoardForm form, RedirectAttributes rda) {
        Board board = form.toEntity();
        boardRepository.save(board);
        rda.addFlashAttribute("msg", "게시판이 성공적으로 수정되었습니다.");
        return "redirect:/board/boardOne?id=" + form.getId();
    }

    @GetMapping("/board/deleteBoard")
    public String deleteBoard(@RequestParam int id, RedirectAttributes rda) {
        Board board = boardRepository.findById(id).orElse(null);

        if (board == null) {
            rda.addFlashAttribute("msg", "게시판 삭제에 실패했습니다.");
            return "redirect:/board/boardList";
        }

        boardRepository.delete(board);
        rda.addFlashAttribute("msg", "게시판이 성공적으로 삭제되었습니다.");
        return "redirect:/board/boardList";
    }
}
