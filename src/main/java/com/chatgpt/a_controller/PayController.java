package com.chatgpt.a_controller;

//支付接口

import com.chatgpt.model.pay.PayDataModel;
import com.chatgpt.service.PayServer;
import com.chatgpt.service.RedisService;
import com.chatgpt.utio.UtioCode.Result;
import com.chatgpt.utio.UtioCode.ResultCode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller  //标识这个是配置层 用来和客户交互的层
@CrossOrigin //让服务器可以跨域
@RequestMapping("/pay")
public class PayController {

    @Resource(name = "payServer")
    public PayServer payServer;

    @Resource(name = "redisService")
    private RedisService redisService;
//    @GetMapping("/getValue/{key}") //查询redis
//    public Object getValue(@PathVariable String key) {
//        return "1";
//    }

//    支付成功跳转的地址
    @RequestMapping("/data")
    @ResponseBody
    public Result payData(PayDataModel pay) { //支付完成拿到的接口地址
        try {

            System.out.println("支付成功界面");
            System.out.println(pay);

            Boolean payOutTradeNo = redisService.getPayOutTradeNo(pay.getOut_trade_no());
            if(payOutTradeNo==true){
                return Result.success(pay);
            }else{
                return Result.failure(ResultCode.No_SUCCESS,"支付失败，用户接口错误，请联系管理员");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.Not___SYSTEM_INNER_ERROR(e.getMessage());
        }
    }

    //    支付的api接口
    @SneakyThrows //抛出异常
    @RequestMapping("/payapi")
    public void payApi(String type,String money,String social_uid, HttpServletResponse response,@RequestHeader("Authorization") String jwt) { //支付完成拿到的接口地址
        System.out.println("支付API接口");
        try {
            PayDataModel payDataModel=new PayDataModel();

            if(type.equals("alipay")||type.equals("wxpay")){
                payDataModel.setType(type); //支付方式 支付宝
            }else{
                System.out.println("其他支付方式");
                payDataModel.setType("alipay"); //支付方式  支付宝
            }

            String orderNumber = payServer.Y_orderNumber();
            payDataModel.setOut_trade_no(orderNumber); //订单号
            payDataModel.setName("购买商品");
            payDataModel.setMoney(money); //支付金额


            Boolean aBoolean = redisService.setPayOutTradeNo(payDataModel, jwt);//保存到redis里面
            if(aBoolean==false){ //支付的redis异常
                System.out.println("支付接口异常！");
                response.sendRedirect("www.baidu.com?message=Payment interface exception"); //重定向到百度网页 如果支付接口崩溃的话
            }


            response.sendRedirect(payServer.payApi(payDataModel,social_uid)); //重定向到支付网页
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("www.baidu.com?message=Payment interface exception"); //重定向到百度网页 如果支付接口崩溃的话


//            return null;
        }
    }




}






















//
//
//    @PostMapping("/login")
//    @ResponseBody
////     , @RequestHeader("Content-Type") String Content 获取请求头
//    public Result login(@RequestBody JSONObject json) { //登入界接口
//        String name1 = json.getString("username");
//        String password1 = json.getString("password");
//
//        LoginModel loginModel = new LoginModel(name1, password1);
//        System.out.println("访问!" + loginModel);
//
//        System.out.println(name1 + "  ---     " + password1);
//
//        String loginNamePassword = JwtUtil.loginNamePassword(name1, loginModel);
//
//        return Result.success(loginNamePassword); //返回签名
//
//    }
