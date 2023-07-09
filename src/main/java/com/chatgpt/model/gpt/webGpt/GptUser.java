package com.chatgpt.model.gpt.webGpt;
import java.util.ArrayList;
import java.util.List;
// 用来保存用户的信息
public class GptUser {
    public List<G2_SonModel> message=new ArrayList<>();

    public GptUser(List<G2_SonModel> message) {
        this.message = message;
    }

    public List<G2_SonModel> getMessage() {
        return message;
    }

    public void setMessage(List<G2_SonModel> message) {
        this.message = message;
    }


}
