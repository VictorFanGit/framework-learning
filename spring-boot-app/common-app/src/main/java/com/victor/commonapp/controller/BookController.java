package com.victor.commonapp.controller;

import com.victor.commonapp.ServiceStarter;
import com.victor.commonapp.entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceStarter.class);

    @PostMapping("/book")
    public String update(@RequestBody(required = false) Book book) {
        System.out.println("=====book============");
        System.out.println(book);
        if(book != null) {
            System.out.println(book.toString());
        }
        return "ok";
    }


}
