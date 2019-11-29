package com.hx199513.community.community.controller;

import com.hx199513.community.community.dto.CommentDTO;
import com.hx199513.community.community.dto.QuestionDTO;
import com.hx199513.community.community.enums.CommentTypeEnum;
import com.hx199513.community.community.service.CommentService;
import com.hx199513.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id")Integer id, Model model){
        List<CommentDTO> comments=commentService.listByType(id,CommentTypeEnum.QUESTION.getType());
        //累加阅读数
        questionService.incView(id);
        QuestionDTO questionDTO=questionService.getById(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        return "question";
}

}
