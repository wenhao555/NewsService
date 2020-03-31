package com.demo.springboot_vue.entities;

import lombok.Data;

@Data
public class User {
    private String account;
    private String password;
    private String name;
    private String sex;
    private Boolean admin;
    private String image;
    private String birth;
}
