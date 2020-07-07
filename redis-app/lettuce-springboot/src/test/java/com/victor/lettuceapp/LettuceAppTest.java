package com.victor.lettuceapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LettuceAppTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LettuceAppTest.class);

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Test
    public void test1() {
        String key = "book";
        Book book = new Book();
        book.setName("Java");
        book.setAuthor("Caffe");
        redisTemplate.opsForValue().set(key, book);
        Book book2 = (Book) redisTemplate.opsForValue().get(key);
        LOGGER.info("get book: " + book2.toString());
        redisTemplate.delete(key);
        redisTemplate.opsForHash().put(key, "java", "java book");
        Object val = redisTemplate.opsForHash().get(key, "java");
        System.out.println(val);
    }
}