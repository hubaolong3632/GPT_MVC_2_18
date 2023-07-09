package com.chatgpt.a_controller;


import com.chatgpt.service.RedisService;
import com.chatgpt.utio.UtioCode.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


//获取用户信息等等操作
@Controller  //标识这个是配置层 用来和客户交互的层
@CrossOrigin //让服务器可以跨域
@RequestMapping("/user")
public class UserController {


    @Resource(name = "redisService")
    public RedisService redisService;




    @RequestMapping("/personal")  // 用户个人信息
    @ResponseBody
    public Result loginQQ(@RequestHeader("Authorization") String jwt) { //登入界接口1 聚合登入拿到请求头里面的参数

        System.out.println("查询用户个人信息");
        try{
            return Result.success(redisService.getUserJWT(jwt)) ;

        }catch (Exception e){
            e.printStackTrace();
            return Result.Not___SYSTEM_INNER_ERROR( e.getMessage()) ;
        }

    }
}
















//
//
//    @PostMapping("/login")
//    @ResponseBody
////     , @RequestHeader("Content-Type") String Content 获取请求头
//    public Result login(@RequestBody JSONObject json) { //登入界接口
//        String name1 = json.getString("username");
//        String password1 = json.getString("password");
//
//        LoginModel loginModel = new LoginModel(name1, password1);
//        System.out.println("访问!" + loginModel);
//
//        System.out.println(name1 + "  ---     " + password1);
//
//        String loginNamePassword = JwtUtil.loginNamePassword(name1, loginModel);
//
//        return Result.success(loginNamePassword); //返回签名
//
//    }
