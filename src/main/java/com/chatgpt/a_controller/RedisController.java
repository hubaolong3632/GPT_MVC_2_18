package com.chatgpt.a_controller;

import com.chatgpt.service.RedisService;
import com.chatgpt.utio.UtioCode.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/getValue/{key}") //查询redis
    public Object getValue(@PathVariable String key) {
        System.out.println("执行查询");
        return redisService.getValue(key);
    }

    @PostMapping("/setValue") //添加redis
    public void setValue(@RequestParam String key, @RequestParam String value) {
        System.out.println("执行搜索");

        redisService.setValue(key, value);
    }


    @PostMapping("/getSocial_uid") //添加redis
    @ResponseBody
    //    通过social_uid查询当前账号的信息
    public Result getUserJWT(String social_uid) {
        System.out.println("查询redis");

        return Result.success(redisService.getUserJWT(social_uid));


    }
}