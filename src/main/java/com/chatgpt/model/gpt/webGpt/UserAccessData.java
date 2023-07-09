package com.chatgpt.model.gpt.webGpt;

import java.util.Map;

//用户访问量等数据记录
public class UserAccessData {
    private long A_userSum;
    private long A_APISum;
    private Map<String, GptUser> mapUser;

    public UserAccessData(long a_userSum, long a_APISum, Map<String, GptUser> mapUser) {
        A_userSum = a_userSum;
        A_APISum = a_APISum;
        this.mapUser = mapUser;
    }

    public long getA_userSum() {
        return A_userSum;
    }

    public void setA_userSum(long a_userSum) {
        A_userSum = a_userSum;
    }

    public long getA_APISum() {
        return A_APISum;
    }

    public void setA_APISum(long a_APISum) {
        A_APISum = a_APISum;
    }

    public Map<String, GptUser> getMapUser() {
        return mapUser;
    }

    public void setMapUser(Map<String, GptUser> mapUser) {
        this.mapUser = mapUser;
    }
}
