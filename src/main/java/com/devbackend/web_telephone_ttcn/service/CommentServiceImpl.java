package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.entity.Comments;
import com.devbackend.web_telephone_ttcn.repository.CommentRepository;
import com.devbackend.web_telephone_ttcn.repository.ProductRepository;
import com.devbackend.web_telephone_ttcn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    @Override
    public boolean addComment(Long idProduct, Long idUser, String content) {
        Comments comment = new Comments();
        comment.setContent(content);
        comment.setUser(userRepository.findById(idUser).get());
        comment.setProduct(productRepository.findById(idProduct).get());
        comment.setIsActive(0);
        comment.setCreatedDate(new Date());
        if(commentRepository.save(comment) != null)
            return true;
        else
            return false;
    }

    @Override
    public List<Comments> getComments() {
        return commentRepository.findAll();
    }

    @Override
    public void confirmComment(Long id) {
        Comments comment = commentRepository.findById(id).get();
        comment.setIsActive(1);
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comments> getCommentsConfirmed(Long idProduct) {
        return commentRepository.getComments(idProduct);
    }
}
