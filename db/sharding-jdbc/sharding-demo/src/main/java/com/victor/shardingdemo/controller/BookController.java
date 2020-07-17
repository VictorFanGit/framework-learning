package com.victor.shardingdemo.controller;

import com.victor.shardingdemo.entity.Book;
import com.victor.shardingdemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getBookList();
    }

    @GetMapping("/book/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/book")
    public Map<String, String> addBookInfo(@RequestBody Book book) {
        HashMap<String, String> map = new HashMap<>();
        bookService.addNewBook(book);
        map.put("result", "ok");
        return map;
    }
}
