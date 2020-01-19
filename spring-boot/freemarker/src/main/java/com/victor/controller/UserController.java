package com.victor.controller;

import com.victor.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @GetMapping("/user")
    public String user(Model model) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUserName("Jack>>>" + i);
            user.setAddress("chengdu>>>" + i);
            users.add(user);
        }
        model.addAttribute("users", users);
        return "user";
    }
}
