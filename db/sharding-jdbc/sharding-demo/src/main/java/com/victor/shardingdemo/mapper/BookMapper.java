package com.victor.shardingdemo.mapper;

import com.victor.shardingdemo.entity.Book;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookMapper {
    @Select("select * from book")
    List<Book> getAllBooks();

    @Select("select * from book where name=#{name}")
    Book queryBookByName(String name);

    @Select("select * from book where id=#{id}")
    Book queryBookById(long id);

    @Select("delete from book where name=#{name}")
    void deleteBookByName(String name);

    @Insert("insert into book (id, name, count) values (#{id}, #{name}, #{count})")
    void insertBook(Book book);
}
