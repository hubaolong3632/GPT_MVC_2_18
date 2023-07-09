package com.chatgpt.model.pay;

import lombok.Data;

//支付接口函数
@Data
public class PayDataModel {
    private String pid;
    private String trade_no;
    private String out_trade_no;
    private String type;
    private String name;
    private String money;
    private String trade_status;
    private String sign;
    private String sign_type;


    public PayDataModel() {
    }

    public PayDataModel(String pid, String trade_no, String out_trade_no, String type, String name, String money, String trade_status, String sign, String sign_type) {
        this.pid = pid;
        this.trade_no = trade_no;
        this.out_trade_no = out_trade_no;
        this.type = type;
        this.name = name;
        this.money = money;
        this.trade_status = trade_status;
        this.sign = sign;
        this.sign_type = sign_type;
    }
}
//    http://00000.work:5555/pay/data
//    ?pid=1335&
//    trade_no=Y2023061722001988446&
//    out_trade_no=Ypayment_a284a556ca83428ead1c&
//    type=alipay
//    &name=
//    c&money=0.01
//    &trade_status=TRADE_SUCCESS&
//    sign=6d69c1ccbcff5417338997516ec1d346
//    &sign_type=MD5
