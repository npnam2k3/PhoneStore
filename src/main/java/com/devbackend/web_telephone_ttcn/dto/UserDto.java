package com.devbackend.web_telephone_ttcn.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(message = "Username không được để trống")
    private String username;
    @NotEmpty(message = "Email không được để trống")
    @Email
    private String email;
    @NotEmpty(message = "Mật khẩu không được để trống")
    private String password;
    @NotEmpty(message = "Số điện thoại không được để trống")
    private String phoneNumber;
    @NotEmpty(message = "Họ tên không được để trống")
    private String fullname;
    private int isDelete;
    private String address;

}
