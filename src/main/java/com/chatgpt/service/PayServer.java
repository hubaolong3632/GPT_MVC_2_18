package com.chatgpt.service;

import com.chatgpt.model.pay.PayDataModel;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.*;

@Service
@PropertySource("classpath:configuration.properties") //配置文件注入
public class PayServer {
    @Value("${ip.http}") //注入配置ip地址
    private  String ip;
    @Value("${pay.key}")
    private   String KEY; //支付密钥 绝密
//    private static final String KEY = "CZIOhHZwDA40X0GkrYEs2c8rvpfkUddL"; //支付密钥 绝密

    @Value("${pay.huiai}")
    public  String ym; //跳转
//    public static String ym="http://huiai.love/pay/apisubmit?";//不跳转


    @Resource
    private RedisService redisService;

//    生成随机订单号
    public static String Y_orderNumber(){
        //生成随机数 20位
        UUID uuid = UUID.randomUUID();
        return  "Y_orderNumber"+uuid.toString().replaceAll("-", "").substring(0,20);
    }
//创建支付订单
    public  String payApi(PayDataModel pay,String social_uid){
        System.out.println("后台查询的ip地址为:"+ip);
        HashMap<String,String> params = new HashMap<>();
        params.put("pid", "1335");
        params.put("type", pay.getType()); //支付设置   支付方式：alipay:支付宝,qqpay:QQ钱包,wxpay:微信支付
        params.put("notify_url", "null");//服务器异步通知地址
        params.put("return_url", ip+"pay/data");//服务器页面跳转通知地址
        params.put("out_trade_no",pay.getOut_trade_no()); //订单号
        params.put("name",pay.getName()); //订单名称
        params.put("money",pay.getMoney());//金额
        params.put("sign", getSign(params)); //密钥
        params.put("sign_type", "MD5");


//        保存到支付订单里面
//        redisService.getUserJWT()


      return ym+getHttp(params); //返回支付接口
    }






    //  生成访问链接
    @SneakyThrows // 解析如果出现异常直接抛出 因为肯定是中文所以不可能有异常
    public  String getHttp(HashMap<String,String> params){ //生成拼接字符串
        //利用TreeMap进行排序
        Map<String,String> sortedMap = new TreeMap<>(params);
        //获取键值对
        Set<Map.Entry<String, String>> entrySet = sortedMap.entrySet();

        //遍历并且拼接
        StringBuilder stringA = new StringBuilder();

        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();

            if(key.equals("name")){ //如果name属性 那么修改他的name属性为中文格式
                    entry.setValue(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            if(key != null) {
                stringA.append(key).append("=").append(entry.getValue()).append("&");
            }
        }
        stringA.deleteCharAt(stringA.length() -1); //删掉最后一个多余的字符
        return String.valueOf(stringA); //最后在转换成小写字符


    }


//  根据md5加密原则生成对应的条件 然后通过拼接的方式吧代码通过md5字典序拼接起来最后通过md5一下生成秘钥
    public  String getSign(HashMap<String,String> params) { //生成拼接字符串
        //利用TreeMap进行排序
        Map<String,String> sortedMap = new TreeMap<>(params);

        //获取键值对
        Set<Map.Entry<String, String>> entrySet = sortedMap.entrySet();

        //遍历并且拼接
        StringBuilder stringA = new StringBuilder();
        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            if(key != null) {
                stringA.append(key).append("=").append(entry.getValue()).append("&");
            }
        }
        stringA.deleteCharAt(stringA.length() -1); //删掉最后一个多余的字符


        StringBuilder stringSignTemp = stringA.append(KEY);
//        System.out.println("生成的字符串:"+stringSignTemp.toString());
        String sign = DigestUtils.md5Hex(stringSignTemp.toString()).toUpperCase(); //md5
        return sign.toLowerCase(); //最后在转换成小写字符


    }
}


//    public static void main(String[] args) throws UnsupportedEncodingException {
//        PayServer pay=new PayServer();
//        PayDataModel payDataModel=new PayDataModel();
//
//        payDataModel.setType("alipay");
//        payDataModel.setOut_trade_no(Y_orderNumber());
////        payDataModel.setName(URLEncoder.encode("购买商品", "UTF-8"));
//        payDataModel.setName("购买商品");
//        payDataModel.setMoney("0.01");
//
//
//        System.out.println(pay.payApi(payDataModel));
//
//    }
