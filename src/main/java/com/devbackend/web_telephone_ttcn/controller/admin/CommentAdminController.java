package com.devbackend.web_telephone_ttcn.controller.admin;

import com.devbackend.web_telephone_ttcn.entity.Comments;
import com.devbackend.web_telephone_ttcn.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("admin")
public class CommentAdminController {
    @Autowired
    private CommentService commentService;

    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    @GetMapping("/managerComment")
    public String managerComment(Model model){
        List<Comments> comments = commentService.getComments();
        model.addAttribute("listComment", comments);
        return "/admin/product/comment";
    }

    @GetMapping("/comment/confirm/{id}")
    public String confirmComment(@PathVariable("id") Long idComment, Model model){
        commentService.confirmComment(idComment);
        List<Comments> comments = commentService.getComments();
        model.addAttribute("listComment", comments);
        return "/admin/product/comment";
    }

    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") Long idComment){
        commentService.deleteComment(idComment);
        return "redirect:/admin/managerComment";
    }
}
