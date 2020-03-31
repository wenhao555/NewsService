package com.demo.springboot_vue.entities;

import lombok.Data;

@Data
public class Recommend {
    private int id;
    private String content;
    private String date;
    private String image;
    private String type;
    private String title;
}
