package com.chatgpt.model.gpt.webGpt;
import com.alibaba.fastjson.JSONObject;

public class Usage {
    private int completionTokens;
    private int promptTokens;
    private int totalTokens;

    public Usage(JSONObject json) {
        completionTokens = json.getIntValue("completion_tokens");
        promptTokens = json.getIntValue("prompt_tokens");
        totalTokens = json.getIntValue("total_tokens");
    }

    public int getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
    }

    public int getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }
// 添加 getter 和 setter 方法

}
