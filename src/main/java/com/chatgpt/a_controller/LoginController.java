package com.chatgpt.a_controller;


import com.chatgpt.model.login.LoginCreationModel;
import com.chatgpt.model.login.LoginFromModel;
import com.chatgpt.service.LoginService;
import com.chatgpt.service.RedisService;
import com.chatgpt.utio.UtioCode.Result;
import com.chatgpt.utio.UtioCode.ResultCode;
import com.chatgpt.utio.UtioY;
import com.chatgpt.utio.model.JWTModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller  //标识这个是配置层 用来和客户交互的层
@CrossOrigin //让服务器可以跨域
@RequestMapping("/login")
public class LoginController {

    @Resource(name = "loginService")
    public LoginService loginService;


    @Resource(name = "redisService")
    public RedisService redisService;


    /**判断当前账号是否有填写账号密码*/
    @RequestMapping("/from_user_name")  // 通过账号密码登入
    @ResponseBody
    public Result from_user_name(@RequestHeader("Authorization") String jwt){
        try{
            if(loginService.from_user_name(jwt)==false){ //如果没有绑定
                return Result.failure(ResultCode.No_SUCCESS,"当前账号没有绑定默认信息");
            }else {
                return Result.success("绑定的账号密码不需要管");
            }


        }catch (Exception e){
            e.printStackTrace();
            return Result.Not___SYSTEM_INNER_ERROR(e.getMessage());
        }

    }




    /**GPT测试的直接生成密钥*/
    @RequestMapping("/jwt")  // 通过账号密码登入
    @ResponseBody
    public Result jain_jwt(@RequestHeader("Authorization") String jwt){
        try{


            System.out.println("获取jwt中 当前传输过来的值为"+jwt);
            if(jwt.equals("null")||UtioY.JWT_PAnalysis(jwt)==null){          //如果没有密钥或者当前密钥是错误的那么
                JWTModel jwtData = new JWTModel();  //创建一个jwt实列
                jwtData.setType("sightseer");//游客登入
                jwtData.setSocial_uid(UtioY.Random_number20()); //生成20未随机值用作JWT认证密钥
                String jwt_create = UtioY.JWT_Create("sightseer", jwtData); //拿到wjt生成的安全访问tokey
                return Result.success(jwt_create);

            }else{
                System.out.println("已经存在");
                return Result.success(jwt);
            }






        }catch (Exception e){
            e.printStackTrace();
            return Result.Not___SYSTEM_INNER_ERROR(e.getMessage());
        }

    }





    /**输入账号密码拿到登入的gwt*/
    @RequestMapping("/loginNamePassword")  // 通过账号密码登入
    @ResponseBody
    public Result loginNamePassword(String name,String password){
        try{


            LoginFromModel fromModel = loginService.loginNamePassword(name, password);
            if(fromModel==null){ //如果账号密码不存在
                return Result.failure(ResultCode.No_SUCCESS,"账号或密码错误");
            }

            System.out.println("输入账号密码账号登入成功");
    //           登入成功之后用户数据的保存地点 数据库存在就保存到redis中 如果数据库不存在就创建一个用户
            if(!redisService.IsKey(fromModel.getSocial_uid())){ //判断键是否存在,如果存在那么就不创建  现在这个主要判断键不存在
                redisService.setUserJWT(fromModel); //创建redis
            }

    //                生成JWT
            JWTModel jwtData = new JWTModel();  //创建一个jwt实列
            jwtData.setType(fromModel.getType());//拿到登入方式
            jwtData.setSocial_uid(fromModel.getSocial_uid()); //拿到唯一值
            String jwt_create = UtioY.JWT_Create(fromModel.getNickname(), jwtData); //拿到wjt生成的安全访问tokey
            return  Result.success(jwt_create);
        }catch (Exception e){
                e.printStackTrace();
            return Result.Not___SYSTEM_INNER_ERROR(e.getMessage());
        }
    }




    /**新用户第一次进来需要输入账号密码用于登入   用户注册写的质料 (登入完整添加资料)*/
    @RequestMapping("/UserLogin")  // 第一次进来输入账号密码
    @ResponseBody
    public Result UserLogin(String name,String password,@RequestHeader("Authorization") String jwt){
        if(name==null||name.equals("")){
            return  Result.failure(ResultCode.No_SUCCESS,"请输入账号");
        }else if(password==null||password.equals("")){
            return  Result.failure(ResultCode.No_SUCCESS,"请输入密码");
        }


        try{
            Boolean aBoolean = loginService.UserLogin(name, password, jwt); //添加个人信息
            if(aBoolean==true){
                  return    Result.success("个人信息账号密码填写成功");
            }else {
               return  Result.failure(ResultCode.No_SUCCESS,"jwt错误不存在请联系管理员");
            }

        }catch (Exception e){
            e.printStackTrace();
            return    Result.failure(ResultCode.No_SUCCESS,"用户名已存在请重新添加账号");

        }


    }


    /**登入成功 现在要获取登入的信息*/
    @RequestMapping("/loginYes")  // 通过聚合进行登入
    @ResponseBody
    public Result loginYes(String type,String code){
        try {
            LoginCreationModel model = new LoginCreationModel();
            model.setType(type);
            LoginFromModel fromModel = loginService.loginYes(model, code);//账号密码判断

            if(fromModel.getCode()==-1){
                return Result.failure(ResultCode.No_SUCCESS,"登入失败,code错误!<已经过时/错误>");
            }else if(fromModel.getCode()==-5){
                return Result.failure(ResultCode.No_SUCCESS,"服务器异常未知原因 请检查日志");
            }else{
    //     这里登入成功

                System.out.println("创建redis操作保存里面的内容");

//           登入成功之后用户数据的保存地点 数据库存在就保存到redis中 如果数据库不存在就创建一个用户
                if(!redisService.IsKey(fromModel.getSocial_uid())){ //判断键是否存在,如果存在那么就不创建  现在这个主要判断键不存在

                    if (!loginService.from_user_social_uid(fromModel.getSocial_uid())){ //如果数据库没有数据
                        Integer integer = loginService.addUser(fromModel);//如果不存在就创建一个

                        if(integer!=-1){ //如果插入成功
                            redisService.setUserJWT(fromModel); //创建redis
                        }
                    }else{ //如果数据库中存在这个值
                        redisService.setUserJWT(fromModel); //创建redis
                    }

                }

//                生成JWT
                JWTModel jwtData = new JWTModel();  //创建一个jwt实列
                jwtData.setType(fromModel.getType());//拿到登入方式
                jwtData.setSocial_uid(fromModel.getSocial_uid()); //拿到唯一值
                String jwt_create = UtioY.JWT_Create(fromModel.getNickname(), jwtData); //拿到wjt生成的安全访问tokey

                return  Result.success(jwt_create);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.Not___SYSTEM_INNER_ERROR(e.getMessage());
        }

    }


    @RequestMapping("/polymerization")  // 通过聚合进行登入
    @ResponseBody
    public Result loginQQ(String type) { //登入界接口1 聚合登入

    try{

            if(type.equals("qq")||type.equals("wx")){ //qq和微信登入
                System.out.println("||>>正常登入");
            }else{ //如果出现恶意请求 直接定向到qq登入
                System.out.println("||>>恶意测试");
                type="qq";
            }

            LoginCreationModel creationModel = new LoginCreationModel();
            creationModel.setType(type);
            LoginCreationModel login = loginService.loginCreation(creationModel);//申请到登入接口

            //如果登入接口发生错误
            if(!login.getCode().equals("0")){
                System.out.println("||>>登入接口异常");
                if(login.getCode().equals("-5")){  //聚合登入接口失败
                    System.out.println("||>>登入接口大异常  远程https主机拒绝!!!!!!!!!!!!");
                    return Result.failure(ResultCode.Login_NO_HTTP,login);
                }else{  //参数填写错误 , 可能密码什么的被删掉了
                    return Result.failure(ResultCode.INTERFACE_ADDRESS_INVALID,login);
                }
            }else{ //登入接口成功的话
                System.out.println("生成登录成功");
                return   Result.success(login);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.Not___SYSTEM_INNER_ERROR(e.getMessage());
        }
    }
}
//    @GetMapping("/getValue/{key}") //查询redis
//    public Object getValue(@PathVariable String key) {
//        return "1";
//    }

//    @RequestMapping("/login")  // 通过普通的账号密码登入
//    @ResponseBody
//    public Result login1(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) { //登入界接口1
//        try {
//            System.out.println("步入登入");
//                return Result.success(loginService.abc());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.Not___SYSTEM_INNER_ERROR(e.getMessage());
//
//        }
//    }






















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
