package com.demo.springboot_vue.entities;

import lombok.Data;

@Data
public class News {
    private int id;
    private String title;
    private String type;
    private String content;
    private String date;
    private String image;
}
