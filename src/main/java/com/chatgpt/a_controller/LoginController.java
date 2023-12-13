package com.chatgpt.a_controller;


import com.chatgpt.model.login.LoginCreationModel;
import com.chatgpt.model.login.LoginFromModel;
import com.chatgpt.service.LoginService;
import com.chatgpt.service.RedisService;
import com.chatgpt.utio.UtioCode.Result;
import com.chatgpt.utio.UtioCode.ResultCode;
import com.chatgpt.utio.UtioY;
import com.chatgpt.utio.model.JWTModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller  //标识这个是配置层 用来和客户交互的层
@CrossOrigin //让服务器可以跨域
@RequestMapping("/login")
public class LoginController {

    @Resource(name = "loginService")
    public LoginService loginService;


    @Resource(name = "redisService")
    public RedisService redisService;


    /**
     * 判断当前账号是否有填写账号密码
     */
    @RequestMapping("/from_user_name")  // 通过账号密码登入
    @ResponseBody
    public Result from_user_name(@RequestHeader("Authorization") String jwt) {
        try {
            if (loginService.from_user_name(jwt) == false) { //如果没有绑定
                return Result.failure(ResultCode.No_SUCCESS, "当前账号没有绑定默认信息");
            } else {
                return Result.success("绑定的账号密码不需要管");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.Not___SYSTEM_INNER_ERROR(e.getMessage());
        }

    }


    /**
     * GPT测试的直接生成密钥
     */
    @RequestMapping("/jwt")  // 通过账号密码登入
    @ResponseBody
    public Result jain_jwt(@RequestHeader("Authorization") String jwt) {
        try {


            System.out.println("获取jwt中 当前传输过来的值为" + jwt);
            if (jwt.equals("null") || UtioY.JWT_PAnalysis(jwt) == null) {          //如果没有密钥或者当前密钥是错误的那么
                JWTModel jwtData = new JWTModel();  //创建一个jwt实列
                jwtData.setType("sightseer");//游客登入
                jwtData.setSocial_uid(UtioY.Random_number20()); //生成20未随机值用作JWT认证密钥
                String jwt_create = UtioY.JWT_Create("sightseer", jwtData); //拿到jwt生成的安全访问tokey
                return Result.success(jwt_create);

            } else {
                System.out.println("已经存在");
                return Result.success(jwt);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.Not___SYSTEM_INNER_ERROR(e.getMessage());
        }

    }
}
