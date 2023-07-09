package com.chatgpt.model.login;

import lombok.Data;

//登入成功之后查询用户的一些参数值
@Data
public class LoginYesFromUserModel {
      private String act; //操作名称 login
      private String appid; //操作的id  1144
      private String appkey; //操作的密钥  443ad960e15a318694dce09f221e060b
      private String type; //操作的登入方式     qq
      private String redirect_uri; //操作完成之后跳转地址  http://43.142.237.246:5555/index.html





    public LoginYesFromUserModel() {
    }

    public LoginYesFromUserModel(String act, String appid, String appkey, String type, String redirect_uri) {
        this.act = act;
        this.appid = appid;
        this.appkey = appkey;
        this.type = type;
        this.redirect_uri = redirect_uri;
    }
}
