package com.example.BackendWebbanhang.domain.response.user;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import com.example.BackendWebbanhang.util.constant.GenderEnum;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;

    private RoleUser role; // Thêm thông tin role trả về

    @Getter
    @Setter
    public static class RoleUser {
        private long id;
        private String name;
    }
}