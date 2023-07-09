package com.chatgpt.utio.UtioClass;

import com.chatgpt.utio.UtioY;
import com.chatgpt.utio.model.JWTDatasModel;
import com.chatgpt.utio.model.JWTModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

//    private static final long EXPIRATION_TIME = 31536000000; // 一年
//    private static final long  EXPIRATION_TIME = 2592000000; // 一个月
    private static final long EXPIRATION_TIME = 604800000; // 一周
//    private static final long EXPIRATION_TIME =   86400000; // 一天
    private static final String SECRET = "8520b3d2d13b0d8ace53d6a81f1ca25c8520b3d2d13b0d8ace53d6a81f1ca25c"; // 秘钥 绝密
    private static final String ISSUER = "SK-00000.work"; // 签发者

    //    生成JWT
    public static String JWTCreate(String subject, JWTModel jwtmodel) { //提交面向的对象  和需要保存的数据
        // 设置过期时间
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME); //当前时间加一周
        // 创建 JWT
        String jwt = Jwts.builder()
                .setSubject(subject)  //jwt所面向的对象
                .setExpiration(expiration) //过期时间
//                .setNotBefore(expiration) //设置在当前时间都是过期的 就是不让使用 没事别打开
                .setIssuer(ISSUER)    //定义签发者
                .claim("jwtmodel", UtioY.JSON(jwtmodel)) //对象方式 放入需要保存的参数
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return jwt;
    }


    // 解析 JWT
    public static JWTDatasModel JWTAnalysis(String jwt) {
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET) //放入秘钥
                    .parseClaimsJws(jwt) //放入需要解析的串
                    .getBody();

            JWTDatasModel jwtDatasModel = new JWTDatasModel();
            jwtDatasModel.setSubject(claims.getSubject());  //标识用户的唯一标识id
            jwtDatasModel.setExpiration(claims.getExpiration()); //标识过期时间
            jwtDatasModel.setIssuer(claims.getIssuer());  //标识颁布者
            jwtDatasModel.setJwtmodel(UtioY.JSON_ObjectType(claims.get("jwtmodel",String.class),JWTModel.class));  //获取jwt参数里面放入的值


            return jwtDatasModel;

        }catch (Exception e){
            System.out.println("|001-JWT错误");
            e.printStackTrace();
            return null;   // 如果对于秘钥解析错误那么放回null
        }
    }





    public static void main(String[] args) {

//        String jwt = JwtUtil.JWTCreate("123",new JWTModel(1,"zhansan"));
//        System.out.println(jwt);
//
//        JWTDatasModel jwtDatasModel = JwtUtil.JWTAnalysis(jwt);
//        if (jwtDatasModel!=null) {  //如果jwt不等于null
//            JWTModel jwtmodel = jwtDatasModel.getJwtmodel();
//            System.out.println(jwtmodel.getName());
//        }

//        if (claims != null) {
//
//            LinkedHashMap<String, Object> dataValueMap = (LinkedHashMap<String, Object>) claims.get("data");
//            System.out.println(dataValueMap);
//
//
//            String subject = claims.getSubject(); //标识用户的唯一标识id
//            Date expiration = claims.getExpiration(); //标识过期时间
//            String issuer = claims.getIssuer();//标识颁布者
//
//
//            String jwtmodel = claims.get("jwtmodel",String.class);//获取密码
//
//            System.out.println(subject+"         "+expiration+"         "+issuer+"         "+jwtmodel);
//            // TODO: 在此处编写验证逻辑
//        }else{
//            System.out.println("秘钥错误停止认证");
//        }

    }
}





//         标准注册界面 headers                       公共声明部分        payloads                                                                                        签证信息                                   私有声明部分
//eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJleHAiOjE2ODQ0MTk5OTMsImlzcyI6Ik15Q29tcGFueSIsIm5hbWUiOiLlvKDkuIkiLCJwYXNzd29yZCI6InF3ZTEyMzUifQ.o5pHTRsTkE4GVt-YqYflijkiJjcrSUSe5Tya-0UtFrR1EQYQffMdis53Hycxv54fawJWwoRqLUBHxiJRfM_yMQ