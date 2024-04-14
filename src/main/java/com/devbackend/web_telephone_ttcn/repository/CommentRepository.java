package com.devbackend.web_telephone_ttcn.repository;

import com.devbackend.web_telephone_ttcn.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
    // lấy các comment theo từng sản phẩm đã được duyệt
    @Query(value = "select c from Comments c where c.isActive = 1  and c.product.id = ?1")
    List<Comments> getComments(Long id);
}
