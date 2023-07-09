
package com.chatgpt.model.gpt.webGpt;
public class GPTModio {
    public String parentMessageId;
    public String message;
    public String key;

    public GPTModio(String parentMessageId, String message, String key) {
        this.parentMessageId = parentMessageId;
        this.message = message;
        this.key = key;
    }

    public String getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(String parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
