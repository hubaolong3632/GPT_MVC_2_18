package com.chatgpt.utio.model;


//import lombok.Data;

//import lombok.Data;

import lombok.Data;

@Data
/**用来放入jwt里面的参数  可以进行自定义*/
public class JWTModel {
   private String type; //登入方式
   private String social_uid; //永久保存的值
    private String uid;

    public JWTModel() {
    }

    public JWTModel(String type, String social_uid) {
        this.type = type;
        this.social_uid = social_uid;
    }
}
