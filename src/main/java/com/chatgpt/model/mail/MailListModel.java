package com.chatgpt.model.mail;


import lombok.Data;

@Data
//一个消息
public class MailListModel {
    public String name; // 发送消息的名称
    public String message;//发送消息的内容

    public MailListModel() {
    }

    public MailListModel(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
