package com.chatgpt.model.pay;

import lombok.Data;

//添加一条订单表
@Data
public class PayOrderModel {
    private String social_uid; //绑定的唯一值
    private String order; //订单号
    private String time; //时间
    private String shopping; //名称
    private String pice; //价格

    public PayOrderModel() {
    }

    public PayOrderModel(String social_uid, String order, String time, String shopping, String pice) {
        this.social_uid = social_uid;
        this.order = order;
        this.time = time;
        this.shopping = shopping;
        this.pice = pice;
    }

    public String getSocial_uid() {
        return social_uid;
    }

    public void setSocial_uid(String social_uid) {
        this.social_uid = social_uid;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShopping() {
        return shopping;
    }

    public void setShopping(String shopping) {
        this.shopping = shopping;
    }

    public String getPice() {
        return pice;
    }

    public void setPice(String pice) {
        this.pice = pice;
    }
}
