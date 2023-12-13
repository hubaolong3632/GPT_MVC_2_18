package com.chatgpt.model.gpt.mvcGpt;

//import lombok.Data;

import java.util.Objects;

//获取余下的
//@Data
public class GptBalanceModel {
    private Integer id;
    private String sum;
    private String use;
    private String balance;


    private String date; //放入时间
    private String key;//秘钥
    private String start;//状态


    public Integer key_id=0;
    public Integer key_id_sum=0;


    public GptBalanceModel() {
    }

    public GptBalanceModel(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "GptBalanceModel{" +
                "id=" + id +
                ", sum='" + sum + '\'' +
                ", use='" + use + '\'' +
                ", balance='" + balance + '\'' +
                ", date='" + date + '\'' +
                ", key='" + key + '\'' +
                ", start='" + start + '\'' +
                ", key_id=" + key_id +
                ", key_id_sum=" + key_id_sum +
                '}';
    }

    public GptBalanceModel(String sum, String use, String balance) {
        this.sum = sum;
        this.use = use;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getBalance(){
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GptBalanceModel that = (GptBalanceModel) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
