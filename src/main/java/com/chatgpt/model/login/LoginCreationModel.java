package com.chatgpt.model.login;

import lombok.Data;

@Data
//创建登入接口
public class LoginCreationModel {
   private String code;
   private String msg; //消息
   private String type; //登入方式
   private String url; //创建登入ip
   private String qrcode; //创建登入微信登入

    public LoginCreationModel() {
    }


    public LoginCreationModel(String code) {
        this.code = code;
    }
}
