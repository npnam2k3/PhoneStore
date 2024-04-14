package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.entity.Comments;

import java.util.List;

public interface CommentService {
    boolean addComment(Long idProduct, Long idUser, String content);

    List<Comments> getComments();
    void confirmComment(Long id);
    void deleteComment(Long id);

    // lay comment theo san pham da duoc duyet
    List<Comments> getCommentsConfirmed(Long idProduct);
}
