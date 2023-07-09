package com.chatgpt.model.login;
//登入成功之后返回的参数

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //创建一个空参构造器
public class LoginYesModel {
    public String code;

    public LoginYesModel(String code) {
        this.code = code;
    }
}
