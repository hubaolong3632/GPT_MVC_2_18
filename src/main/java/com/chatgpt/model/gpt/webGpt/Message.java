package com.chatgpt.model.gpt.webGpt;
import com.alibaba.fastjson.JSONObject;

public class Message {
    private String role;
    private String content;

    public Message(JSONObject json) {
        role = json.getString("role");
        content = json.getString("content");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // 添加 getter 和 setter 方法

}
