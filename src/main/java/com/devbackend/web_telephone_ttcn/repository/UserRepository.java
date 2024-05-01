package com.devbackend.web_telephone_ttcn.repository;

import com.devbackend.web_telephone_ttcn.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    @Query(value = "SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1")
    List<User> getAllByRoleId(String roleName);

    // Phan trang
    @Query(value = "SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1")
    Page<User> getAllByRoleIdPageable(String roleName, Pageable pageable);

    // lấy danh sách theo từ khóa tìm kiem
    @Query("select u from User u where u.username like %?1%")
    List<User> searchUser(String keyword);

    //phan trang va tim kiem
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1 AND u.username LIKE %?2%")
    Page<User> searchUserWithUserRole(String roleName, String keyword, Pageable pageable);

    // dem tong so luong user trong he thong
    @Query(value = "SELECT COUNT(u.id) FROM users u JOIN users_roles ur ON u.id = ur.user_id WHERE ur.role_id = ?1", nativeQuery = true)
    int countUsersByRoleId(Long roleId);
}
