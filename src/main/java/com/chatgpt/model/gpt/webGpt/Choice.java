
package com.chatgpt.model.gpt.webGpt;
import com.alibaba.fastjson.JSONObject;

public class Choice {
    private String finishReason;
    private int index;
    private Message message;

    public Choice(JSONObject json) {
        finishReason = json.getString("finish_reason");
        index = json.getIntValue("index");
        message = new Message(json.getJSONObject("message"));
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    // 添加 getter 和 setter 方法

}
