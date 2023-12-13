package com.chatgpt.model.Paid;


import lombok.Data;

//查询当前用户剩余的次数和用户状态
@Data
public class PaidModel {
    public int id;
    public String uid;
    public String jurisdiction;
    public int sum;
}
