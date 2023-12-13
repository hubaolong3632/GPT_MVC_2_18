package com.chatgpt.z_ljx;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.chatgpt.config.RootConfig;
import com.chatgpt.idao.IGptDao;
import com.chatgpt.model.gpt.mvcGpt.GPTConfigurationModel;
import com.chatgpt.model.gpt.mvcGpt.GptBalanceModel;
import com.chatgpt.model.gpt.mvcGpt.GptStaticModel;
import com.chatgpt.model.gpt.webGpt.G2_SonModel;
import com.chatgpt.model.gpt.webGpt.GPT_2测试Model;
import com.chatgpt.model.gpt.webGpt.GptUser;
import com.chatgpt.service.GptService;
import com.chatgpt.service.RedisService;
import com.chatgpt.utio.UtioClass.DateUtio;
import com.chatgpt.utio.UtioClass.IP;
import com.chatgpt.utio.UtioClass.JS;
import com.chatgpt.utio.UtioY;
import com.chatgpt.utio.model.IPRessModel;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("mainText") //和bean区别是一个作用与类一个是作用于方法
public class MainText {


    private static final long serialVersionUID = 1L;

    private   static   List<String> list = new ArrayList<>(); // 待优化：使用List更好地管理API密钥
    private   static   List<String> ip_no = new ArrayList<>(); // 用于记录用户黑名单
    private static   int sum_key = 0; // key 的交换
    public   static  Map<String, GptUser> mapUser=new HashMap<>(); //保存用户访问信息

    public  static   long USER_sum_visit=0; // 记录用户访问数量
    public  static  long API_sum_visit=0; // 记录今日访问量

    public static Map<String,Integer> IPSumMap=new HashMap<>(); //统计各个ip 访问次数

//    private  static  Map<String, GptUser> mapUser=new HashMap<>(); //保存用户访问信息

    public static List<String> listURL=new ArrayList<>();
//    public static int sum_URL=0; //统计URL的

@Test
    protected void doPost() throws ServletException, IOException {

    while (true) {
        try {

            List<G2_SonModel> listGPT = null;


            int sum_sum_sum = 0;
            String userName = "zs";
            String message = "你好";

            System.out.println("\n-----------------------------------------------------");
            if (
                    mapUser.containsKey(userName)) { //存在的时候
                System.out.println("|001>>:存在用户");
                listGPT = mapUser.get(userName).getMessage(); //获取集合
                System.out.println("|003>>:访问数量:" + (listGPT.size() + 1)); //加1因为从0开始

                if (listGPT.size() > 10) { //连续模式下最多10条
                    mapUser.get(userName).setMessage(new ArrayList<>()); //重新创建一个如果超过15条
                    System.out.println("|111>>:超过10条已经重新开始");
                }
                listGPT.add(new G2_SonModel("user", message));

            } else {
                System.out.println("|002>>:新用户访问");
                //创建消息集合
                listGPT = new ArrayList<>();
                listGPT.add(new G2_SonModel("user", message));
                GptUser gptUser = new GptUser(listGPT);
                mapUser.put(userName, gptUser);// 以名字带对象的方法使用

                USER_sum_visit++; //今日访问人数
            }
            System.out.println("msg:" + message);
            API_sum_visit++; //今日访问量

            System.out.println("|--今日访问人数:" + USER_sum_visit + "   今日访问数量:" + API_sum_visit + "--|");


            while (true) {
                try {
//                            GPT_2测试Model g2 = new GPT_2测试Model("gpt-3.5-turbo", listGPT, 0.9, true);
                    GPT_2测试Model g2 = new GPT_2测试Model("gpt-3.5-turbo-16k", listGPT, 0.9, true);
//                            GPT_2测试Model g2 = new GPT_2测试Model("gpt-3.5-turbo-0613", listGPT, 0.9, true);
                    String json = JS.JSONNoDate(g2); //输出测试用例 并且不带时间

                    System.out.println(json);
                    byte[] postData = json.getBytes("UTF-8");
                    String url = "https://openai.1rmb.tk" + "/v1/chat/completions"; //生成请求链接


                    URL serverUrl = new URL(url);
                    HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");


                    //进行连接GPT官网
//                    System.out.println("第:"+sum_key % list.size()+"     |007>> 使用的秘钥是:"+list.get(sum_key % list.size()));

                    String sum_URL = retrunKey().getKey();

//                    conn.setRequestProperty("Authorization", "Bearer " + list.get(sum_key % list.size()) + "");
                    conn.setRequestProperty("Authorization", "Bearer " + sum_URL + "");
//                    sum_key++;
                    conn.setRequestProperty("Connection", "Keep-Alive"); //持续连接
                    conn.setConnectTimeout(200000); //时长
                    OutputStream wr = conn.getOutputStream();
                    wr.write(postData);
                    wr.flush();


                    //收到的数据发送给前端
                    InputStream is = conn.getInputStream();
                    System.out.println("执行输出1");

                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String output;
                    System.out.println("执行输出2");

                    while ((output = br.readLine()) != null) {
//                        System.out.println("执行输出");

                        try {
                            JSONObject js = new JSONObject().parseObject(output.replace("data: ", ""));
                            if (js != null) {
//                                System.out.println(js);
//                                String text = js.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").getString("content").replaceAll("\n", "{{END}}");
                                String text = js.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").getString("content").replaceAll("\n", "{{END}}");
                                if (text != null) {
                                    System.out.println("输出");
//                                    System.out.print(text);
                                }
                            }
//
                        } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    System.out.println("|>>json异常");
                        } catch (Exception e) {  //可能会出现解析的时候一点点事故（间隔发送的）但是没关系咱不管他
//                            System.out.println("发生错误");
//                            e.printStackTrace();
//                            System.out.println("|>>>程序出现异常|-yc001:>>:"+e.getMessage());

                        }
                    }


                    System.out.println("|100---->《发送成功》");

                    break;
                } catch (Exception e) {  //出现其他问题

//                      如果失误5次就停止
                    sum_sum_sum++;
                    if (sum_sum_sum == 5) {
                        System.out.println("终止");
                        return;
                    }
                    System.out.println("次数加1" + sum_sum_sum);

                    System.out.println("|>>>程序出现异常|-yc002:>>:" + e.getMessage());

                    e.printStackTrace();
                }
            }


        } catch (Exception e) { //连接的时候突然客户端关闭
            System.out.println("|>>>程序出现异常|-yc003:>>:" + e.getMessage());

            e.printStackTrace();
        }

    }
}




    static {
        List<GptBalanceModel> balanceList = GptStaticModel.getList;
        balanceList.add(new GptBalanceModel("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"));
        balanceList.add(new GptBalanceModel("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"));
        balanceList.add(new GptBalanceModel("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"));
        balanceList.add(new GptBalanceModel("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"));
        balanceList.add(new GptBalanceModel("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"));
        balanceList.add(new GptBalanceModel("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"));
        balanceList.add(new GptBalanceModel("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"));
        balanceList.add(new GptBalanceModel("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"));
        GptStaticModel.keySum=balanceList.size();
    }


    /**返回一个密钥用于操作  密钥轮换*/
    public GptBalanceModel retrunKey(){

        int keyPresent = GptStaticModel.keyPresent;
        int keySum = GptStaticModel.keySum;

        if(keyPresent==0){
            System.out.println("密钥数量为0个请注意密钥！！");
            return null;
        }
        int keyLong=keyPresent%keySum;//计算出当前是第多少个
//        System.out.println("第:"+keyLong+"个密钥");

        GptStaticModel.keyPresent++;

        return GptStaticModel.getList.get(keyLong); //拿到第n个密钥
    }



    @Test
    public void CheshiGPT(){
        MainText main=new MainText();
        GPTConfigurationModel gptConfig=new GPTConfigurationModel(); //拿到配置模型

        gptConfig.setStream(true);
        gptConfig.setTemperature(0.9);
        gptConfig.setModel("gpt-3.5-turbo-0613");



        List<G2_SonModel> messages=new ArrayList<>();
        messages.add(new G2_SonModel("user", "你好"));
        gptConfig.setMessages(messages);
     String   json = JS.JSONNoDate(gptConfig); //输出测试用例 并且不带时间
int k=0;
        while(true){
            System.out.println("传输过去的数据:");
            System.out.println(json);
            main.abc(json);

            System.out.println("当前执行次数="+(++k));

        }
    }
    public void abc(String   json){
        try {
            byte[] postData = json.getBytes("UTF-8");

            String url = "https://openai.1rmb.tk" + "/v1/chat/completions"; //生成请求链接
//            String url = "https://api.openai.com" + "/v1/chat/completions"; //生成请求链接

            URL serverUrl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");//模拟用户请求

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json");


            String key = retrunKey().getKey();
            System.out.println("使用的密钥为:" + key);
            conn.setRequestProperty("Authorization", "Bearer " + key);

            conn.setRequestProperty("Connection", "Keep-Alive"); //持续连接


            conn.setConnectTimeout(2000); //时长
            OutputStream wr = conn.getOutputStream();
            wr.write(postData);
            wr.flush();
            System.out.println("连接gpt服务器成功");



            //收到的数据发送给前端
            InputStream is = conn.getInputStream();
            System.out.println("设置流请求成功");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            System.out.println("设置传输编码格式成功");
//            StringBuilder sb = new StringBuilder(); //用来最后放入gpt回答的答案



            String output;
            while ((output = br.readLine()) != null) {
//                System.out.println(output);
//                System.out.println("输出");
                try {
                    JSONObject js = new JSONObject().parseObject(output.replace("data: ", ""));
//                    System.out.println(js);
                    if (js != null) {
                        String text = js.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").getString("content");
//                        String text = js.getString("content");
                        System.out.println(js);

                    }
                } catch (NullPointerException e) {
//                    e.printStackTrace();
                    System.out.println("结束1");
                } catch (JSONException e) {
//                    e.printStackTrace();
                    System.out.println("结束2");

                } catch (Exception e) {  //可能会出现解析的时候一点点事故（间隔发送的）但是没关系咱不管他
                    System.out.println("发生错误");
                    e.printStackTrace();
                    System.out.println("|>>>程序出现异常|-yc001:>>:" + e.getMessage());

                }
            }







        }catch (Exception e){
            e.printStackTrace();
        }
    }




    @Test
    /**日志文件测试*/
    public void abc1(){
        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
        MainText main = (MainText) context.getBean("mainText");

    }

    @Resource
    RedisService res;
    @Test
    /**测试数据的保存*/
    public void abc332(){
        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
        MainText main = (MainText) context.getBean("mainText");
        main.template1.delete("gptMessage:zs");


        main.res.setUserMessageList("zs",new G2_SonModel("user","你好1"));
        main.res.setUserMessageList("zs",new G2_SonModel("user","你好2"));
        main.res.setUserMessageList("zs",new G2_SonModel("user","你好3"));
        main.res.setUserMessageList("zs",new G2_SonModel("user","你好4"));
        main.res.setUserMessageList("zs",new G2_SonModel("user","你好5"));
        main.res.setUserMessageList("zs",new G2_SonModel("user","你好6"));


        List<G2_SonModel> zs = main.res.getUserMessageList("zs");
        System.out.println(zs);


    }


    @Test
    public void cs2(){
        StringBuilder sb = new StringBuilder("Hello");
        sb.append("你");
        sb.append("好");
        sb.append("我");
        sb.append("的");
        sb.append("回答");

        G2_SonModel assistant = new G2_SonModel("assistant", sb.toString());



        String json = UtioY.JSON(assistant);
        System.out.println(json);

    }


    @Test
    /**gpt生成模型接口测试*/
    public void abcTest(){
        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
        MainText main = (MainText) context.getBean("mainText");
        GptService gptService = main.gptService1;
        RedisTemplate<String, Object> template = main.template1;

//List
        template.delete("list:1");//不论是谁反正都给你删了


        template.opsForList().rightPush("list:1","测试1"); // 添加几个元素
        template.opsForList().rightPush("list:1","测试2");
        template.opsForList().rightPush("list:1","测试3");
        template.opsForList().rightPush("list:1","测试4");
        template.opsForList().rightPush("list:1","测试5");
        template.opsForList().rightPush("list:1","测试6");
        template.opsForList().rightPush("list:1","测试7");
        template.opsForList().rightPush("list:1","测试8");
        template.opsForList().rightPush("list:1","测试9");
        template.opsForList().rightPush("list:1","测试10");
        template.opsForList().rightPush("list:1","测试11");
        template.opsForList().rightPush("list:1","测试12");

        long listSize = template.opsForList().size("list:1"); //获取总长度


        if(listSize>10){ //如果数量超过10个那么移除最开始那个
            template.opsForList().leftPop("list:1");
            template.opsForList().leftPop("list:1");
        }




        System.out.println("总长度:"+listSize);
        List<Object> range =template.opsForList().range("list:1", 0, listSize);
        for (Object o : range) {
            System.out.println(o);
        }





//        String zs = gptService.gptUserMessageList("zs", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJRREIiLCJleHAiOjE2ODgyNzY3NjAsImlzcyI6IlNLLTAwMDAwLndvcmsiLCJqd3Rtb2RlbCI6IntcInNvY2lhbF91aWRcIjpcIm9Oa2pYNkhXN1hoZkZRNW5KbGt3TlRrY0tGekVcIixcInR5cGVcIjpcInd4XCJ9In0.lEdP2rW08vEQ_16NTRAWw9ECbKWfUJszcHwdbOaqCJUp1xdUf8pJtwACZ7lkvZEFtSo0ZVfkjTSTzOpx8Q7pvw");


//        System.out.println(zs);

    }


    @Test
    /**批量添加*/
    public void abc33(){
        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
        MainText main = (MainText) context.getBean("mainText");
        GptService gptService = main.gptService1;
//        List<GptBalanceModel> gptBalanceModels = gptService.insert_gpt_key("账号08dqzcjt1f1qavoamg@aaas8.icu密码VA0NwWqNK邮箱密码Asd123456写代码用的：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\n" +
//                "账号cqpjs108rcwjum3qow@aaas8.icu密码AOAYfF2CO邮箱密码Asd123456写代码用的：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\n" +
//                "账号siy0ddwgppnuw9jytb@aaas8.icu密码kMU7DdCyL邮箱密码Asd123456写代码用的：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\n" +
//                "账号hm0a30oyysopadye75@topmail.icu密码tW1jKKXtV邮箱密码Asd123456写代码用的：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\n" +
//                "账号xsjfzu5y877hz8faa1@aaas8.icu密码mrrNVyun7邮箱密码Asd123456写代码用的：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\n" +
//                "账号cq90czxna0lkegomdg@paaas.icu密码zBJ9RehiV邮箱密码Asd123456写代码用的：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\n" +
//                "账号vxirpfa17b94ft7tq8@aaas8.icu密码a5IJdvvgo邮箱密码Asd123456写代码用的：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\n" +
//                "账号ddhl34zu8jnh4zvg89@pophotmail.icu密码DL9zWsGbZ邮箱密码Asd123456写代码用的：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\n" +
//                "账号tov2s0bg822rzqc4yw@paaas.icu密码qceqhdVqw邮箱密码Asd123456写代码用的：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\n" +
//                "账号qz2z566bdj4562qyj8@topmail.icu密码jDaGvrNf3邮箱密码Asd123456写代码用的：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");
//

//        List<GptBalanceModel> gptBalanceModels = gptService.insert_gpt_key("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  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");

//        System.out.println(gptBalanceModels);

    }


//    static {
//        List<GptBalanceModel> balanceList = GptStaticModel.getList;
//
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        GptStaticModel.keySum=balanceList.size();
//    }
//    密钥轮换
    @Test
    public void lh1(){

        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
        MainText main = (MainText) context.getBean("mainText");
        for(int i=0;i<50;i++){
            GptBalanceModel gptBalanceModel = main.gptService1.  retrunKey();
            System.out.println(gptBalanceModel);

        }
    }


    @Test
    public void Redis11122() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
        MainText main = (MainText) context.getBean("mainText");
        RedisTemplate<String, Object> template = main.template1;

        System.out.println("清理所有IP次数成功");
        String pattern = "ipNumber-*";
        Set<String> keys = template.keys(pattern);
        template.delete(keys);

        System.out.println("清理所有用户次数成功");
        String userKeySum = "userKeyNumber-*";
        Set<String> userKeySumSet = template.keys(userKeySum);
        template.delete(userKeySumSet);



    }








    @Test
    public void Redis111() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
        MainText main = (MainText) context.getBean("mainText");
        RedisTemplate<String, Object> template = main.template1;

        String ipNumber = "ipNumber-" + "127.0.0.1";
        String userKeyNumber = "userKeyNumber-" + "323232323232";

        // 获取Hash操作对象
        HashOperations<String, String, Object> hashOperations = template.opsForHash();

        // 判断是否存在当前值
        boolean exists = hashOperations.hasKey(ipNumber, "sum"); // 判断sum字段是否存在
        if (exists) {
            System.out.println("存在当前键");
            // 存在当前值，自增sum属性
            hashOperations.increment(ipNumber, "sum", 1);
        } else {
            System.out.println("不存在当前键");
            // 不存在当前值，创建map并设置sum属性为1
            hashOperations.put(ipNumber, "sum", 1);
        }

        Long count = template.opsForValue().increment(userKeyNumber, 1); // 自增 1
        hashOperations.increment(ipNumber, userKeyNumber, 1); //将用户放进哈希表并且自增1

        Object o = template.opsForValue().get(userKeyNumber);
        System.out.println("查询里面的值"+o);

        // 查询键对应的值
        Object sumUser = hashOperations.get(ipNumber, userKeyNumber);

        // 打印查询结果
        System.out.println("当前用户访问数量: " + sumUser);

        // 查询哈希类型键的全部内容
        System.out.println("当前ip的账号和次数");
//        System.out.println(hashContent);

        Object sum = hashOperations.get(ipNumber, "sum");
        System.out.println(sum);

        // 删除键
//        template.delete(ipNumber);
//        template.delete(userKeyNumber);
    }

//redis测试
//    @Test
//    public void Redis111(){
//
//        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
//        MainText main = (MainText) context.getBean("mainText");
//        RedisTemplate<String, Object> template = main.template1;
//
//        String ipNumber ="ipNumber-"+ "127.0.0.1";
//        String userKeyNumber ="userKeyNumber-"+"323232323232";
//        // 获取Hash操作对象
//        HashOperations<String, String, Object> hashOperations = template.opsForHash();
//
//
//
//        // 判断是否存在当前值
//        boolean exists = hashOperations.hasKey(ipNumber, "sum"); //判断sum字段是否存在
//        if (exists) {
//            System.out.println("存在当前键");
//            // 存在当前值，自增sum属性
//            hashOperations.increment(ipNumber, "sum", 1);
//        } else {
//            System.out.println("不存在当前键");
//            // 不存在当前值，创建map并设置sum属性为1
//            hashOperations.put(ipNumber, "sum", 1);
//        }
//
//
//////        判断当前账号是否存在
////        if(template.hasKey(userKeyNumber)){
////            System.out.println("账号在的");
////        }else{
////            System.out.println("账号不在的");
////        }
//
//
//        Long value = template.opsForValue().increment(userKeyNumber, 1); // 自增 1
//        hashOperations.increment(ipNumber, userKeyNumber, 1); //他里面有的用户放进去并且自增1
//
//
//
//        List<Object> range =template.opsForHash().range(ipNumber ,0, -1); //查找当前用户的所有信息
//        System.out.println("当前ip的账号和次数");
//        System.out.println(range);
//
//        // 查询键对应的值
//        Object sumUser = template.opsForValue().get(ipNumber);
//
//        // 打印查询结果
//        System.out.println("用户访问数量为:"+sumUser);
//
//
////        template.delete(ipNumber);
////        template.delete(userKeyNumber);
//
//    }







    @Autowired
    public IGptDao getDao;
    @Resource
    public GptService gptService1;
    @Test
    /**多条测试*/
    public void abc111(){
        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
        MainText main = (MainText) context.getBean("mainText");
        IGptDao IgetDao = main.getDao;
        GptService gptService = main.gptService1;

//        GptBalanceModel gpt_from = new GptBalanceModel();
//        gpt_from.setKey("sk-000001");
//        gpt_from.setStart("启用");
//        gpt_from.setBalance("待添加");
//        gpt_from.setSum("待添加");
//        gpt_from.setUse("待添加");
//        gpt_from.setDate(UtioY.Date_getDate());
//        IgetDao.insert_gpt_key(gpt_from);


//
//        List<GptBalanceModel> balanceList=new ArrayList<>();
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//        balanceList.add(new GptBalanceModel("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"));
//
//
//
//        List<GptBalanceModel> balance = gptService.gptFromBalancesList(balanceList);
//        System.out.println("输出测试");
//
//        for (GptBalanceModel gptBalanceModel : balance) {
//            gptBalanceModel.setStart("启用");
//            gptBalanceModel.setDate(UtioY.Date_getDate());
//            IgetDao.insert_gpt_key(gptBalanceModel);
////            System.out.println("key:"+gptBalanceModel.getKey());
////            System.out.println("一共:"+gptBalanceModel.getSum());
////            System.out.println("已使用:"+gptBalanceModel.getUse());
////            System.out.println("余额:"+gptBalanceModel.getBalance());
////            System.out.println("\n");
//        }




//        查询
        GptBalanceModel gptModel = new GptBalanceModel();
//        gptModel.setStart("停止");
        List<GptBalanceModel> gptBalanceModels = IgetDao.from_gpt_keys(gptModel);
//        System.out.println(gptBalanceModels);


//        修改
        System.out.println("步入修改");
//        gptBalanceModels.get(0).setStart("启用");
//        gptBalanceModels.get(1).setStart("启用");
//        gptBalanceModels.get(2).setStart("启用");
//        IgetDao.update_gpt_keys(gptBalanceModels);



//        GptBalanceModel ggg3 = gptBalanceModels.get(1);
//        ggg3.setId(2);
//        ggg3.setKey("测试密钥");
//        System.out.println(ggg3);
//
//
//        Integer integer = IgetDao.update_gpt_key(ggg3);
//        System.out.println(integer);


    }







//    @Test
//    /**测试Data注解自动*/
//    public void abc2(){
//        DataText dataText = new DataText();
//        String abc = dataText.getAbc();
//        System.out.println(abc);
//    }










//
//
//    @Test
//    public   void Duixiang(){
//        String str= """
//                {"id":1,"name":"zhansan"}
//                """;
//        try{
//            ObjectMapper objectMapper = new ObjectMapper();
//            JWTModel user = objectMapper.readValue(str, JWTModel.class);
//            System.out.println(user);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }



    @Test
    public   void Duixiang1() throws IOException {
        try {
            String url = "http://whois.pconline.com.cn/ipJson.jsp?ip=49.75.7.213&json=true";
            Document doc = Jsoup.connect(url).get();
            String jsonStr = doc.text();

            JSONObject jsonObject =JSONObject.parseObject(jsonStr);
            String ip            = jsonObject.getString("ip");
            String province         = jsonObject.getString("pro");
            String provinceCode         = jsonObject.getString("proCode");
            String city                 = jsonObject.getString("city");
            String cityCode             = jsonObject.getString("cityCode");
            String address              = jsonObject.getString("addr");
            IPRessModel ipRessModel = new IPRessModel(ip, province, provinceCode, city, cityCode, address);
            System.out.println(ipRessModel);

//            System.out.println("IP: " + ip);
//            System.out.println("省份: " + province);
//            System.out.println("省份代码: " + provinceCode);
//            System.out.println("城市: " + city);
//            System.out.println("城市代码: " + cityCode);
//            System.out.println("地址: " + address);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @SneakyThrows
    @Test
    public void abc(){ //测试生成状态码
//        LoginCreationModel loginCreationModel=new LoginCreationModel();
//        loginCreationModel.setType("qq");
//        loginCreation(loginCreationModel);
    }



    @Test
//    gpt账号拆解
    public void abc12(){ //测试生成状态码
        String input = "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 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" +
                "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 "  +
                "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 " +
                "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 ";
        Pattern pattern = Pattern.compile("sk-[A-Za-z0-9]+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String key = matcher.group();
            System.out.println(key);
        }

    }


    public static void main(String[] args) {
//        record PersonRecord(String name,int age){}
//        PersonRecord zhang = new PersonRecord("zhang", 1);
//        System.out.println(zhang.name());
//        System.out.println(zhang.age());
//        ArrayList<PersonRecord> objects = new ArrayList<>();
//
//
//        String text = """
//    ~    Hello,
//    ~    This is a text block.
//    ~    It can contain multiple lines without using escape characters or concatenation operators.
//    ~    """;
//        System.out.println(text);
//
//        String str= """
//                  你好
//                - "id": "001",
//            ~"name": "alex",
//                 "email": "alex@example.com"
//                    """;
//        System.out.println(str);
    }








    @Autowired
    private RedisTemplate< String, Object > template1;

    @Test
    /**Redis测试*/
    public void abcRedis(){
        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
        MainText MainText = (MainText) context.getBean("mainText");
        RedisTemplate<String, Object> template = MainText.template1;


//        Map
        template.opsForHash().put("map:1","name","zhangsan"); //添加
        template.opsForHash().put("map:1","password","123456");
        template.opsForHash().put("map:1","sex","男");
//        template.opsForHash().put("map:1","sex",new JWTModel("222","333"));

        Object name = template.opsForHash().get("map:1", "name"); //查询
        System.out.println(name);

//        template.opsForHash().delete("map:1", "name");   // 删除map中指定的键值对

        template.opsForHash().hasKey("map:1", "password"); //查询是否存在此键值对


        template.opsForHash().size("map:1"); //查询里面的数量
        Map<Object, Object> entries = template.opsForHash().entries("map:1");//获取所有键值对

        for (Object o : entries.keySet()) {
            System.out.println("键："+o);
            System.out.println("值："+entries.get(o));
        }

        //        template.expire("map:1", 10, TimeUnit.SECONDS);  //10秒过期
        System.out.println(name);


//List

//        template.opsForList().leftPush("list:1","zhangsand"); // 添加几个元素
//        template.opsForList().leftPush("list:1","5200303");
//        template.opsForList().leftPush("list:1","123432123");
//
//        Long listSize = template.opsForList().size("list:1"); //获取总长度
//        System.out.println(listSize);
//
//        template.opsForList().set("list:1", 0, "xxxxxx"); //修改里面的值从左往右的
//
//        System.out.println(template.opsForList().range("list:1", 0, listSize)); //查询里面所有的值



//        System.out.println(template.opsForList().remove("list:1", 0, "zhangsand")); //删除所有张三的值
//          template.delete("list:1");//不论是谁反正都给你删了















//        String
//        template.opsForValue().set("2", "3123123123");
//        template.expire("2", 10, TimeUnit.SECONDS);  //10秒过期

        /**
         *
         * - TimeUnit.MILLISECONDS：毫秒
         * - TimeUnit.MINUTES：分钟
         * - TimeUnit.HOURS：小时
         * - TimeUnit.DAYS：天
         *
         * */

//
//        Object o =template.opsForValue().get("2");
//        System.out.println(o);

    }







}
