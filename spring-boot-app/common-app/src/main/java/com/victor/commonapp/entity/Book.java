package com.victor.commonapp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Book {
    private String name;
    private String author;
    private Date publishDate;
    private double price;
}
