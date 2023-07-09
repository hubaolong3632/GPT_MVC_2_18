package com.chatgpt.model.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

//数据的查询代码
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //设置返回不为空的
public class LoginFromModel {
    private Integer code; //状态码
    private String msg;//状态
    private String type; //登入方式 qq
    private String social_uid; //永久保存的qq识别码
    private String access_token; //30天内可以使用的查询状态
    private String nickname; // 登入人的名称
    private String faceimg;  //登入人的头像
    private String location; //登入人的地址
    private String gender; //性别
    private String ip; //登入ip


    private String name; //登入账号的用户名
    private String password; //登入账号的密码

    private String mail; //登入账号的邮箱
    private String qq; //登入账号的qq



    public LoginFromModel() {
    }

    public LoginFromModel(Integer code) {
        this.code = code;
    }
}
