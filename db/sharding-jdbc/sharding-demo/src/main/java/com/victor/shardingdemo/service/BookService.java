package com.victor.shardingdemo.service;

import com.victor.shardingdemo.entity.Book;
import com.victor.shardingdemo.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookMapper bookMapper;

    public List<Book> getBookList() {
        return bookMapper.getAllBooks();
    }

    public Book getBookByName(String name) {
        return bookMapper.queryBookByName(name);
    }

    public Book getBookById(long id) {
        return bookMapper.queryBookById(id);
    }

    public void deleteBookByName(String name) {
        bookMapper.deleteBookByName(name);
    }

    public void addNewBook(Book book){
        bookMapper.insertBook(book);
    }
}
