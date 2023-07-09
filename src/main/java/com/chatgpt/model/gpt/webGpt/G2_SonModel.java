package com.chatgpt.model.gpt.webGpt;

import lombok.Data;

@Data
public class G2_SonModel {
   public String role;
   public String content;


    public G2_SonModel(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public G2_SonModel() {
    }
}
