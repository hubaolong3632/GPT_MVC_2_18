package com.chatgpt.service;

import com.chatgpt.idao.IUserDao;
import com.chatgpt.model.gpt.mvcGpt.GptNumberModel;
import com.chatgpt.model.gpt.webGpt.G2_SonModel;
import com.chatgpt.model.login.LoginFromModel;
import com.chatgpt.utio.UtioY;
import com.chatgpt.utio.model.JWTDatasModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@PropertySource("classpath:configuration.properties") //配置文件注入
public class RedisService {

    @Resource
    private RedisTemplate< String, Object > template;

    @Resource
    private IUserDao userDao; //用户表的操作


    @Value("${gpt.sumMessage}")
    private Integer sumMessage; //标识可以放多少条数据连续


//    删除键
    public void deleteKey(String key){
        template.delete(key);
    }

//    使用key 方法判断当前键是否存在不存在就往里面添加 如果存在那么就清理
//    添加用户访问次数
   public  Boolean addNumber(String ipNumber,String userKeyNumber){
        try {

       // 获取Hash操作对象
       HashOperations<String, String, Object> hashOperations = template.opsForHash();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
   }




    @Value("${gpt.ipSum}")
    private Integer ipSum; //标识同一个ip可以放多少条数据连续


    @Value("${gpt.UserSum}")
    private Integer UserSum; //标识同一个用户可以放多少条数据连续

//   判断当前用户是否超过设置的值如果超过了那么就拒绝访问
   public GptNumberModel sumIPUser(String ipNumber, String userKeyNumber){




       // 获取Hash操作对象
       HashOperations<String, String, Object> hashOperations = template.opsForHash();

       // 判断是否存在当前值
       boolean exists = hashOperations.hasKey(ipNumber, "sum"); // 判断sum字段是否存在
       if (!exists) {
           System.out.println("不存在当前键");
           // 不存在当前值，创建map并设置sum属性为1
           hashOperations.put(ipNumber, "sum", 1);
       }

// 查询键对应的值
//       Integer numberIp = Integer.valueOf((String)hashOperations.get(ipNumber, "sum"));
       Long numberIp = hashOperations.increment(ipNumber, userKeyNumber, 1);//将用户放进哈希表并且自增1
       Long numberUser =template.opsForValue().increment(userKeyNumber, 1);//返回当前用户的操作次数并且当前访问完成之后次数加1

       // 打印查询结果
       System.out.println("当前ip访问数量: " + numberIp+"          用户访问数量:"+numberUser);


//       判断如果大于访问次数那么就限制访问 如果小于那么就可以继续访问
       if(numberIp>ipSum||numberUser>UserSum){
           return new GptNumberModel(numberIp,numberUser,false);
       }else{
           return new GptNumberModel(numberIp,numberUser,true);
       }

   }


//    定时清理所有的剩余次数  查询所有键值对对应的值并且保存到数据库今日访问量
    public int cleaningKey(){
            System.out.println("清理所有IP次数成功");
            String pattern = "ipNumber-*";
            Set<String> keys = template.keys(pattern);
            template.delete(keys);

             System.out.println("清理所有用户次数成功");
             String userKeySum = "userKeyNumber-*";
             Set<String> userKeySumSet = template.keys(userKeySum);
             template.delete(userKeySumSet);

            return keys.size();


    }


//    GPT的文章内容管理保存文章
    public Boolean setUserMessageList(String social_uid,G2_SonModel g2){
        try{
            String gptMessageKey = "gptMessage:" + social_uid;
            System.out.println("参数为："+gptMessageKey);

            long listSize = template.opsForList().size(gptMessageKey); //获取总长度

            if(listSize>sumMessage*2){ //如果数量超过设置的接口
                template.opsForList().leftPop(gptMessageKey);
                template.opsForList().leftPop(gptMessageKey);
                System.out.println("数量超标重新掉了前一条");
            }
           template.opsForList().rightPush(gptMessageKey, UtioY.JSON(g2));// 添加用户的信息
//            System.out.println("输出"+aLong);



        return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**查找对象的参数*/
    public List<G2_SonModel> getUserMessageList(String social_uid){
        System.out.println(" 查找的reids参数social_uid:"+social_uid);
        String gptMessageKey = "gptMessage:" + social_uid;

        List<Object> range =template.opsForList().range(gptMessageKey ,0, -1); //查找当前用户的所有信息
        List<G2_SonModel> listG2=new ArrayList<>();

//        谁知道呢，反正暂时没找到可以一下全部改成对象的方法 万一以后学到了或许可以修改一下这里，毕竟能不用for循环最好了
        for (Object jsonMessage : range) {
            listG2.add(UtioY.JSON_ObjectType((String)jsonMessage,G2_SonModel.class));
//            System.out.println(listG2);
        }

        return listG2;
    }








    public Object getValue(final String key) {
        System.out.println("执行查询");
        return template.opsForValue().get(key);
    }

//    添加value的参数
    public void setValue(final String key, final String value) {
        template.opsForValue().set(key, value);
        System.out.println("执行添加");
        template.expire(key, 10, TimeUnit.SECONDS);  //10秒过期
    }



    //    添加用户的各种参数     用来查询用户的值   不需要 String jwt ,
    public Boolean setUserJWT(LoginFromModel  login) {
        try {
            String social_uid = login.getSocial_uid(); //拿到他永久保存的值进行记录
            template.opsForHash().put(social_uid,"type",login.getType()); //登入方式
            template.opsForHash().put(social_uid,"social_uid",login.getSocial_uid()); //永久保存的登入码
            template.opsForHash().put(social_uid,"Nickname",login.getNickname()); //姓名
            template.opsForHash().put(social_uid,"faceimg",login.getFaceimg()); //头像
//            template.opsForHash().put(social_uid,"ip",login.getIp()); //ip地址
            template.expire(social_uid, 3, TimeUnit.DAYS);  //登入日志三天过期  防止redis炸掉
            return true;
        }catch (Exception e){
            System.out.println("|>>login错误");
            e.printStackTrace();
        }

        return false;
    }




        //    通过数据库永久保存的值查询当前账号的信息
        public LoginFromModel getUserJWT(String jwt){

            JWTDatasModel jwtDatasModel = UtioY.JWT_PAnalysis(jwt);
            String social_uid=  jwtDatasModel.getJwtmodel().getSocial_uid(); //拿到jwt里面的参数值

            if (template.hasKey(social_uid)){//判断jwt是否存在
                LoginFromModel login=new LoginFromModel();

                login.setType(       (String) template.opsForHash().get(social_uid,"type")); //登入方式
                login.setSocial_uid( (String) template.opsForHash().get(social_uid,"social_uid")); //永久保存的登入码
                login.setNickname(   (String) template.opsForHash().get(social_uid,"Nickname")); //姓名
                login.setFaceimg(    (String)template.opsForHash().get(social_uid,"faceimg")); //头像



                return login;
            }else{
                LoginFromModel login = userDao.from_user(social_uid);
                if(login!=null){ //如果数据库有这条数据
                    System.out.println("redis没有去数据库查看是否存在");
                    setUserJWT(login); //创建jwt

                    return login; //返回查询到的值
                }else{
                    System.out.println("发现数据库也没查找到这个人");
                    return null;
                }
            }


        }


//        判断key 是否存在
        public Boolean IsKey(String key){

        return template.hasKey(key);
        }


}