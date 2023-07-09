

package com.chatgpt.model.gpt.webGpt;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ChatCompletion {
    private long created;
    private Usage usage;
    private String model;
    private String id;
    private Choice[] choices;
    private String object;

    public ChatCompletion(JSONObject json) {
        created = json.getLongValue("created");
        usage = new Usage(json.getJSONObject("usage"));
        model = json.getString("model");
        id = json.getString("id");
        object = json.getString("object");

        JSONArray choicesJson = json.getJSONArray("choices");
        choices = new Choice[choicesJson.size()];
        for (int i = 0; i < choicesJson.size(); i++) {
            JSONObject choiceJson = choicesJson.getJSONObject(i);
            choices[i] = new Choice(choiceJson);
        }
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Choice[] getChoices() {
        return choices;
    }

    public void setChoices(Choice[] choices) {
        this.choices = choices;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    // 添加 getter 和 setter 方法

}

