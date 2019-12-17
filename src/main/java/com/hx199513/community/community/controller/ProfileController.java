package com.hx199513.community.community.controller;

import com.hx199513.community.community.dto.PaginationDTO;
import com.hx199513.community.community.model.User;
import com.hx199513.community.community.service.NotificationService;
import com.hx199513.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public  String profile(@PathVariable(name="action")String action,
                           Model model, HttpServletRequest request,
                           @RequestParam(name="page",defaultValue = "1") Integer page,
                           @RequestParam(name="size",defaultValue = "2") Integer size){

        User user=(User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }


       if ("questions".equals(action)){
           model.addAttribute("section","questions");
           model.addAttribute("sectionName","我的提问");
           PaginationDTO paginationDTO=questionService.list(user.getId(),page,size);
           model.addAttribute("pagination",paginationDTO);
       }else if ("replies".equals(action)){
           PaginationDTO paginationDTO=notificationService.list(user.getId(),page,size);
           Integer unreadCount=notificationService.unreadCount(user.getId());
           model.addAttribute("pagination",paginationDTO);
           model.addAttribute("section","replies");
           model.addAttribute("sectionName","最新回复");
           model.addAttribute("unreadCount",unreadCount);
       }

        return "profile";
    }
}
