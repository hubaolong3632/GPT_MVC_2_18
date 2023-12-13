package com.chatgpt.a_controller;

import com.chatgpt.idao.IUserDao;
import com.chatgpt.model.Paid.PaidModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class RedirectController {
    @Autowired
    IUserDao userDao;


    @RequestMapping("/redirect")
    @ResponseBody
    public String redirect() {
        Integer integer = userDao.update_user_uid("735661020ff74d8fad91");
        System.out.println(integer);
        return integer.toString();
    }



}