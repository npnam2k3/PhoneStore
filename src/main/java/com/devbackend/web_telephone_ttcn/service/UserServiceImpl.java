package com.devbackend.web_telephone_ttcn.service;

import com.devbackend.web_telephone_ttcn.dto.UserDto;
import com.devbackend.web_telephone_ttcn.entity.Role;
import com.devbackend.web_telephone_ttcn.entity.User;
import com.devbackend.web_telephone_ttcn.repository.RoleRepository;
import com.devbackend.web_telephone_ttcn.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setFullname(userDto.getFullname());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setIsDelete(0);

        //lấy tên quyền trong bảng roles với quyền: ROLE_USER
        Role role = roleRepository.findByName("ROLE_USER");
        if(role == null){
            //nếu chưa có quyền ROLE_USER -> tạo mới
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.getAllByRoleId("ROLE_USER");
        //lặp qua tất cả user lấy từ db chuyển thành dto và trả về dạng list
        return users.stream()
                        .map((user) -> mapToUserDto(user))
                                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).get();
        return mapToUserDto(user);
    }

    // kích hoạt tài khoản
    @Override
    public boolean deleteOn(Long id) {
        User user = userRepository.findById(id).get();
        if(user!=null){
            user.setIsDelete(0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // vô hiệu hóa tài khoản
    @Override
    public boolean deleteOff(Long id) {
        User user = userRepository.findById(id).get();
        if(user!=null){
            user.setIsDelete(1);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkPasswordExists(String password, Long id) {
        User user = userRepository.findById(id).get();
        if(passwordEncoder.matches(password, user.getPassword())){
            return true;
        }
        return false;
    }

    @Override
    public void changePassword(Long id, String password) {
        User user = userRepository.findById(id).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    // cap nhat thong tin ben phia nguoi dung
    @Override
    public void updateCustomer(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).get();
        user.setFullname(userDto.getFullname());
        user.setAddress(userDto.getAddress());
        userRepository.save(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> list = userRepository.searchUser(keyword);
        return list.stream().map(user->mapToUserDto(user)).collect(Collectors.toList());
    }

    @Override
    public Page<User> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 4);
        return userRepository.getAllByRoleIdPageable("ROLE_USER", pageable);
//        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchAndPage(String keyword, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 4);
        return userRepository.searchUserWithUserRole("ROLE_USER",keyword, pageable);
    }

//    @Override
//    public boolean checkCartOfUser(Long idUser) {
//        User user = userRepository.findById(idUser).get();
//        if(user.getCart() != null){
//            return true;
//        }
//        return false;
//    }


    //Chuyển từ entity -> dto
    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFullname(user.getFullname());
        userDto.setUsername(user.getUsername());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setIsDelete(user.getIsDelete());
        userDto.setAddress(user.getAddress());
        return userDto;
    }

    //hàm thêm quyền mới khi chưa tồn tại quyền đó
    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }
}
