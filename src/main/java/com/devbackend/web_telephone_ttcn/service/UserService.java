package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.dto.UserDto;
import com.devbackend.web_telephone_ttcn.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    void saveUser(UserDto userDto);
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();
    UserDto findById(Long id);
    boolean deleteOn(Long id);
    boolean deleteOff(Long id);

    boolean checkPasswordExists(String password, Long id);

    void changePassword(Long id, String password);
    void updateCustomer(UserDto userDto);

    List<UserDto> searchUser(String keyword);

    //phan trang
    Page<User> getAll(Integer pageNo);

    //phan trang va tim kiem
    Page<User> searchAndPage(String keyword, Integer pageNo);

//    boolean checkCartOfUser(Long idUser);
}
