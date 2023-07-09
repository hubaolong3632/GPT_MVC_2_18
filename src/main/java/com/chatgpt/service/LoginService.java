package com.chatgpt.service;


import com.alibaba.fastjson.JSONObject;
import com.chatgpt.idao.ILoginDao;
import com.chatgpt.idao.IUserDao;
import com.chatgpt.model.login.LoginCreationModel;
import com.chatgpt.model.login.LoginFromModel;
import com.chatgpt.model.login.LoginYesFromUserModel;
import com.chatgpt.utio.UtioY;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;

//判断是否有当前账号
@Service
@PropertySource("classpath:configuration.properties") //配置文件注入

public class LoginService {


    @Resource(name = "ILoginDao")
    ILoginDao loginDao;
    @Resource(name = "IUserDao")
    IUserDao iUserDao;

    @Value("${ip.http}") //注入配置ip地址
    private  String ip;

    @Value("${login.act}") //注入配置ip地址
    private String act;

    @Value("${login.appID}") //注入 聚合登入的id
    private String appID;

    @Value("${login.appKey}") //注入 聚合登入的 key
    private String appKey;


    /**判断账号密码是否填写*/
    public Boolean from_user_name(String jwt){

       String social_uid= UtioY.JWT_PAnalysis(jwt).getJwtmodel().getSocial_uid();
        String name = loginDao.from_user_name(social_uid).getName();
        if (name==null||name.equals("")) { //如果没有绑定
            return false;
        }else {
            return true;
        }

    }

    /**输入账号密码拿到登入的gwt   这里是拿到用户资料*/
    public LoginFromModel loginNamePassword(String name, String password){
        return loginDao.from_user_passowr(name, password);


    }


    /**新用户第一次进来需要输入账号密码用于登入   用户注册写的质料 (登入完整添加资料)*/
    public Boolean UserLogin(String name, String password,String jwt){
        System.out.println(name+"     "+password+"     "+jwt);
            name=  UtioY.HTML_replace(name);
            password= UtioY.HTML_replace(password);

            String social_uid = UtioY.JWT_PAnalysis(jwt).getJwtmodel().getSocial_uid(); //拿到他所保存的唯一值
        System.out.println("拿到的JWT："+social_uid);
            Integer integer = loginDao.update_UserPassowrdLogin(name, password, social_uid); //修改当前账号绑定的账号密码
        System.out.println(integer);
            if(integer==1){
                return true;
            }else{
                return false;
            }
    }



    /**插入到数据*/
    public Integer addUser(LoginFromModel login){ //添加一个用户
        System.out.println("添加用户操作");
        System.out.println(login);
        return iUserDao.insert_user(login);
    }

    /**查询当前数据库是否存在这条数据*/
    public Boolean from_user_social_uid(String social_uid){
//        System.out.println(iUserDao.from_user(social_uid));
        if(iUserDao.from_user(social_uid)!=null){ //如果存在就不创建
            return true;
        }
        return false;

    }

    /** 登入成功之后进行哪个操作的代码*/
    public LoginFromModel loginYes(LoginCreationModel logType, String code){
        try{
//            System.out.println("密钥"+code);
            LoginYesFromUserModel login=new LoginYesFromUserModel();
            login.setAct(act);
            login.setAppid(appID);
            login.setAppkey(appKey);
            login.setType(logType.getType()); //登入方式
            String url = getLoginUserUrl(login,code);
//            System.out.println(url);

            //    获取登入的json格式 并且转换成类
            Document doc = Jsoup.connect(url).ignoreContentType(true).get(); //忽略请求体
            String jsonStr = doc.text();
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
//            System.out.println(jsonObject);

            LoginFromModel loginCreationModel = UtioY.JSON_ObjectType(jsonObject.toJSONString(), LoginFromModel.class);
            System.out.println(loginCreationModel);

            return loginCreationModel;
        }catch (Exception e){
            e.printStackTrace();
            return new LoginFromModel(-5); //表示异常错误 状态码
        }

    }




    /**创建登入接口*/
    public LoginCreationModel loginCreation(LoginCreationModel logType){
//        System.out.println("登入的接口"+ip);
        try{
//            排序对应的登入值 生成url
            LoginYesFromUserModel login=new LoginYesFromUserModel();
            login.setAct("login");
            login.setAppid(appID);
            login.setAppkey(appKey);
            login.setType(logType.getType()); //登入方式
            login.setRedirect_uri(URLEncoder.encode(ip+"login/loginYes", "UTF-8"));//跳转地址
            String url = generateLoginUrl(login);

            //    获取登入的json格式 并且转换成类
            Document doc = Jsoup.connect(url).ignoreContentType(true).get(); //忽略请求体
            String jsonStr = doc.text();
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            LoginCreationModel loginCreationModel = UtioY.JSON_ObjectType(jsonObject.toJSONString(), LoginCreationModel.class);
//            System.out.println(loginCreationModel);

            return loginCreationModel;
        }catch (Exception e){
            e.printStackTrace();
            return new LoginCreationModel("-5"); //表示异常错误
        }
    }



    @Value("${login.urlHTML}") //注入 聚合登入 的访问ip
    private String urlHTML;

//    生成访问链接
    private String generateLoginUrl(LoginYesFromUserModel login) {
//        String baseUrl = urlHTML;
        String queryParams = String.format("act=%s&appid=%s&appkey=%s&type=%s&redirect_uri=%s", login.getAct(), login.getAppid(), login.getAppkey(),login.getType(), login.getRedirect_uri());
        return urlHTML + "?" + queryParams;
    }

    //    生成登入成功请求用户个人信息
    private String getLoginUserUrl(LoginYesFromUserModel login,String code) {
//        String baseUrl = urlHTML;
        String queryParams = String.format("act=%s&appid=%s&appkey=%s&type=%s&code=%s",  login.getAct(), login.getAppid(), login.getAppkey(),login.getType(),code);
        return urlHTML + "?" + queryParams;
    }




}
