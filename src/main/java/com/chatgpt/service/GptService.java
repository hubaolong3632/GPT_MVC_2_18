package com.chatgpt.service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.chatgpt.idao.IGptDao;
import com.chatgpt.idao.IUserDao;
import com.chatgpt.model.Paid.PaidModel;
import com.chatgpt.model.gpt.mvcGpt.GPTConfigurationModel;
import com.chatgpt.model.gpt.mvcGpt.GptBalanceModel;
import com.chatgpt.model.gpt.mvcGpt.GptNumberModel;
import com.chatgpt.model.gpt.mvcGpt.GptStaticModel;
import com.chatgpt.model.gpt.webGpt.G2_SonModel;
import com.chatgpt.utio.GPT.GptSumModel;
import com.chatgpt.utio.UtioClass.JS;
import com.chatgpt.utio.UtioCode.Result;
import com.chatgpt.utio.UtioCode.ResultCode;
import com.chatgpt.utio.UtioY;
import com.chatgpt.utio.model.JWTDatasModel;
import org.apache.ibatis.annotations.Param;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:configuration.properties") //配置文件注入
public class GptService {

    @Value("${gpt.url}")
     private   String gptUrl; //反向代理访问地址访问gpt官网

    @Value("${gpt.jwtUrl}")
     private   String jwtUrl; //服务器登入地址

    @Resource
    public  IGptDao iGptDao;

    @Resource
    public  RedisService redisService;


//


/** 测试数据一定一定得删除*/
    static {
        List<GptBalanceModel> balanceList = GptStaticModel.getList;
        balanceList.add(new GptBalanceModel("sess-hl4jogZIfRtQOa7cbkPNGvdigxg70KzsXg1CcvAq"));
        balanceList.add(new GptBalanceModel("sess-YPe84x1bFE2xZkV6jzYAB7cUU2EqO9uUP0rb2OZY"));
        balanceList.add(new GptBalanceModel("sess-4I4vgWaNX6FMNkDYs9NME0ivlVY4cMYyo4Y0Ds07"));
        GptStaticModel.keySum=balanceList.size();
    }
    /** 测试数据一定一定得删除*/
    /** 测试数据一定一定得删除*/
    /** 测试数据一定一定得删除*/
    /** 测试数据一定一定得删除*/
    /** 测试数据一定一定得删除*/
    /** 测试数据一定一定得删除*/



      @Resource
      private   GPTConfigurationModel gptConfig; //拿到配置模型


    @Autowired
    IUserDao userDao;


//    查看当前密钥剩余次数
    public GptSumModel fromSumGpt(String jwt){
        GptSumModel gptSum=new GptSumModel();
        try{
            Document doc = Jsoup.connect(jwtUrl)
                    .header("Authorization",jwt)
                    .ignoreContentType(true).post(); //忽略请求体
            String jsonStr = doc.text();
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            int sum = jsonObject.getJSONObject("data").getIntValue("sum");
            String name = jsonObject.getJSONObject("data").getString("name");
            //System.out.println(name+":用户：当前剩余次数:"+sum);
            gptSum.setSum(sum);
            gptSum.setName(name);
            if(sum>0){ //如果密钥次数大于0次
                gptSum.setSumBool(true);
            }else{
                gptSum.setSumBool(false);
            }
            return  gptSum;
        }catch (Exception e){
            e.printStackTrace();
            gptSum.setSum(-1);
            gptSum.setMessage(e.getMessage()); //记录错误
            gptSum.setSumBool(null);
            return gptSum;

        }

    }


    /**查询用户剩余的次数*/
    public PaidModel from_user_uid(String uid){
        return userDao.from_user_uid(uid);
    }



//    给当前余下的钱减少
    public Boolean update_user_uid(String uid) {
        Integer integer = userDao.update_user_uid(uid);
        if(integer==1){
            return true;
        }
        return false;
    }



// 流操作那些的都在这里了前面是新加的需求


    /**非流回答*/
    public G2_SonModel gpt_NO_Stream(String message, String jwt, HttpServletRequest request, HttpServletResponse response, boolean streamBool) {
        //System.out.println("进入");
        G2_SonModel g2_sonModel =null;
        try {

            String social_uid = null;
            try{
                social_uid= UtioY.JWT_PAnalysis(jwt).getJwtmodel().getUid(); //拿到用户昵称
                //System.out.println("----------内容-----------------");
                //System.out.println(UtioY.JWT_PAnalysis(jwt).getJwtmodel());
                //System.out.println(social_uid);
            }catch (Exception e){ //如果密钥过期了
                return new G2_SonModel("root","你的密钥已经过期请重新登入 如不会可联系站长进行操作");
            }



            //System.out.println("继续往下走");

                //System.out.println("不用流");
                g2_sonModel=  gptHTTPS_NoStarm(gptUserMessageList(message, social_uid));


            if(g2_sonModel.getRole().equals("429")){
                //System.out.println("密钥超过5个使用不了");
                return null;
            }
            redisService.setUserMessageList(social_uid, g2_sonModel);  //保存到redis里面
            //System.out.println("保存数据成功");

//             保存到数据库里面和添加访问次数
            addUserMessage(social_uid,request,response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return g2_sonModel;
    }












    /**流式回答*/
    public G2_SonModel gptStream(String message, String jwt, HttpServletRequest request, HttpServletResponse response, boolean streamBool) {
        //System.out.println(jwt);
        G2_SonModel g2_sonModel =null;
        try {
//            设置请求头
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("text/event-stream;charset=UTF-8");
            response.flushBuffer();
            PrintWriter writer = response.getWriter(); //申请发送


            String social_uid = null;
            try{
                JWTDatasModel jwtDatasModel = UtioY.JWT_PAnalysis(jwt);//拿到用户昵称
                //System.out.println(jwtDatasModel);


                social_uid=jwtDatasModel.getJwtmodel().getUid();

                System.out.println("当前密钥："+social_uid);
                //System.out.println(jwtDatasModel.getJwtmodel());
                //System.out.println("----------xxx------------\n");
            }catch (Exception e){ //如果密钥过期了
                writer.write("你的账号登入已经过期请重新登入账号"); //写出到网页 换行替换成{{end}}
                writer.flush();//发送操作
                return null;
            }

//                账号登入检查今天提问次数和内容
            Result result = boolMessage(message,writer,social_uid,request,jwt);
            if(result.getCode()!=1){
                //System.out.println("发送了触发事件");
                    return null;
            }
            //System.out.println("继续往下走");


//            流输出
                g2_sonModel = gptHTTPS(gptUserMessageList(message, social_uid), response,request,writer);




             if(g2_sonModel.getRole().equals("429")){
                 //System.out.println("密钥超过5个使用不了");
                 writer.write("服务器密钥轮询超过5个使用不了，请（重新发送）如果还出现此问题，请联系管理员"); //写出到网页 换行替换成{{end}}
                 writer.flush();//发送操作
                 return null;
             }
             redisService.setUserMessageList(social_uid, g2_sonModel);  //保存到redis里面
             //System.out.println("保存数据成功");



//             保存到数据库里面和添加访问次数
            addUserMessage(social_uid,request,response);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return g2_sonModel;
    }



    /**进行判断一些内容
     * @param message
     * @param writer
     * @param social_uid
     * @param request */
public Result boolMessage(String message, PrintWriter writer, String social_uid, HttpServletRequest request,String jwt){

    String ipNumber ="ipNumber-"+ UtioY.IP_getIp(request);
    String userKeyNumber ="userKeyNumber-"+social_uid;
    GptNumberModel gptNumberModel = redisService.sumIPUser(ipNumber, userKeyNumber);

//    GptSumModel sumModel = new GptSumModel();
//    sumModel.setSum(100);
//    sumModel.setSumBool(true);
//    sumModel.setName("111");
    GptSumModel sumModel = fromSumGpt(jwt);
    if(sumModel.getSumBool()==null){
        writer.write("出现的异常为:"+sumModel.getMessage()+"    账号为："+userKeyNumber+"{{END}}       请携带这个错误信息联系管理员QQ:911412667\""); //写出到网页 换行替换成{{end}}
        writer.flush();//发送操作
        return Result.failure(ResultCode.No_SUCCESS,"出现了异常");
    }
    if(gptNumberModel.getSumBol()==true&&sumModel.getSumBool()==true){ //满足次数没有超过限制 并且服务器那边也具有次数



           if(message.equals("emptyemptyempty")){
               redisService.deleteKey("gptMessage:" + social_uid);
               //System.out.println("执行了清空");
               writer.write("清空成功"); //写出到网页 换行替换成{{end}}
               writer.flush();//发送操作
               return Result.failure(ResultCode.No_SUCCESS,"清空了历史消息");
           }


       }else {
        if(sumModel.getSumBool()==false){
            writer.write("你的发送次数超过限制 请点击上面的用户按钮按照步骤进行充值使用!    账号为："+sumModel.getName()+"{{END}}     剩余次数为:"+sumModel.getSum()+" ");
            writer.flush();//发送操作
            return Result.failure(ResultCode.No_SUCCESS,"余儿不足");
        }
           writer.write("你的发送次数超过本软件限制，请联系管理员解除!当前区域同IP访问次数为：   "+gptNumberModel.getNumberIp()+"  你的账号访问次数为:      "+gptNumberModel.getNumberUser()
                   +"{{END}}           IP为："+ipNumber+"{{END}}      账号为："+sumModel.getName()+"{{END}}       请携带这个错误信息联系管理员QQ:911412667");
           writer.flush();//发送操作
           return Result.failure(ResultCode.No_SUCCESS,"次数超过限制");
       }





    return Result.success("消息成功");
}



    @Value("${gpt.sumWord}")
    public long sumWord;//字数限制

//    生成请求参数
    public String gptUserMessageList(String message, String social_uid){

//        保存消息 如果消息出现问题比如redis炸了那么可以看看
        Boolean aBoolean = redisService.setUserMessageList(social_uid, new G2_SonModel("user", message));

        if(aBoolean==true){
            //System.out.println("通过此id查询："+social_uid);
            List<G2_SonModel> messageList = redisService.getUserMessageList(social_uid);


//            用的都是同一个对象万一炸了呢,不知道用户少应该不会炸
            gptConfig.setMessages(messageList); //拿到数据放入到请求参数里面
            String json = JS.JSONNoDate(gptConfig); //输出测试用例
//            //System.out.println("提交的json格式为:");
//            //System.out.println(json);


//            如果数量超过设置的指定字数  重新更新当前对象
            if(json.length()>sumWord){
                 redisService.deleteKey("gptMessage:" + social_uid); //删除当前所有保存的
                 redisService.setUserMessageList(social_uid, new G2_SonModel("user", message)); //重新添加参数

//                重新生成json格式数据
                messageList = redisService.getUserMessageList(social_uid);
                gptConfig.setMessages(messageList); //拿到数据放入到请求参数里面
                json = JS.JSONNoDate(this.gptConfig); //输出测试用例 并且不带时间
                //System.out.println("超过:提交的json格式为:");
                //System.out.println(json);
                //System.out.println("数量超标");
            }

            return json;
        }

        return null;





    }



    //    保存用户所有操作的数据 以及访问次数等等
    private void addUserMessage(String social_uid, HttpServletRequest request, HttpServletResponse response) {
        //        在这里进行统计用户访问次数
        String ipNumber ="ipNumber-"+ UtioY.IP_getIp(request);
        String userKeyNumber ="userKeyNumber-"+social_uid;
        redisService.addNumber(ipNumber,userKeyNumber);

    }



    //    进行官网的请求  请求内容和http
    public G2_SonModel gptHTTPS_NoStarm(String messageJson) {
        //System.out.println(messageJson);
        int sum_sum_sum = 0; //统计当前失败了多少次

        while (true) {
            try {
                byte[] postData = messageJson.getBytes("UTF-8");
                //System.out.println("当前字数为:" + postData.length);

//        在这里判断是否为空


                String url = gptUrl + "/v1/chat/completions"; //生成请求链接
                //System.out.println("请求链接:" + url);


//            请求官网拿到数据
                URL serverUrl = new URL(url);
                HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");//模拟用户请求

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");


                String key = retrunKey().getKey();
                //System.out.println("使用的密钥为:" + key);
                conn.setRequestProperty("Authorization", "Bearer " + key);
                conn.setRequestProperty("Connection", "Keep-Alive"); //持续连接
                conn.setConnectTimeout(200000); //时长
                OutputStream wr = conn.getOutputStream();
                wr.write(postData);
                wr.flush();
                //System.out.println("连接gpt服务器成功");

                //收到的数据发送给前端
                InputStream is = conn.getInputStream();
//                //System.out.println("设置流请求成功");
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                //System.out.println("设置传输编码格式成功");
                StringBuilder sb = new StringBuilder(); //用来最后放入gpt回答的答案


                String output;
                //System.out.println("开始发送");
                while ((output = br.readLine()) != null) {
                    try {
                        JSONObject js = new JSONObject().parseObject(output.replace("data: ", ""));
                        if (js != null) {
                            String text = js.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").getString("content");
                            if (text != null) {
                                System.out.print(text);
                                sb.append(text);
                            }
                        }
                    } catch (NullPointerException e) {
                    } catch (JSONException e) {
                    } catch (Exception e) {  //可能会出现解析的时候一点点事故（间隔发送的）但是没关系咱不管他
                        //System.out.println("发生错误");
                        e.printStackTrace();
                        //System.out.println("|>>>程序出现异常|-yc001:>>:" + e.getMessage());

                    }
                }
                String content = sb.toString();


                if(content.length()<1){ // 如果回答的字数小于1个那么就出现问题了就重新访问


                    //System.out.println("回答字数少于1位");
                    sum_sum_sum++;

                    if (sum_sum_sum == 5) {    // 如果失误5次就停止
                        //System.out.println("|||》》》》终止");
                        return new G2_SonModel("429", "no");
                    }
                   continue;
                }

                //System.out.println("发送消息成功");
                return new G2_SonModel("assistant", sb.toString());
            } catch (Exception e) {
                //System.out.println("|||》》》》进行重复查找");
//                    应该发出警报有密钥不正确了
                e.printStackTrace();
                sum_sum_sum++;


//                把密钥更改为异常 并且删除密钥


                if (sum_sum_sum == 5) {    // 如果失误5次就停止
                    //System.out.println("|||》》》》终止");
                    return new G2_SonModel("429", "no");
                }
            }


        }

    }













//    进行官网的请求  请求内容和http
    public G2_SonModel gptHTTPS(String messageJson, HttpServletResponse response, HttpServletRequest request, PrintWriter writer) {


        //System.out.println(messageJson);

        //        生成数据的时候如果redis炸了
        if (messageJson == null) {
            writer.write("服务器已炸了，请联系管理员！！！！！");
            writer.flush();
        }

        int sum_sum_sum = 0; //统计当前失败了多少次


        while (true) {

            GptBalanceModel gptKeyModel = retrunKey();
            try {

                byte[] postData = messageJson.getBytes("UTF-8");
                //System.out.println("当前字数为:" + postData.length);

//        在这里判断是否为空


                String url = gptUrl + "/v1/chat/completions"; //生成请求链接
                //System.out.println("请求链接:" + url);


//            请求官网拿到数据
                URL serverUrl = new URL(url);
                HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
               conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");//模拟用户请求

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");


                String key = gptKeyModel.getKey();
                //System.out.println("使用的密钥为:" + key);
                conn.setRequestProperty("Authorization", "Bearer " + key);

                conn.setRequestProperty("Connection", "Keep-Alive"); //持续连接


                conn.setConnectTimeout(200000); //时长
                OutputStream wr = conn.getOutputStream();
                wr.write(postData);
                wr.flush();
                //System.out.println("连接gpt服务器成功");

                //收到的数据发送给前端
                InputStream is = conn.getInputStream();
//                //System.out.println("设置流请求成功");
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                //System.out.println("设置传输编码格式成功");
                StringBuilder sb = new StringBuilder(); //用来最后放入gpt回答的答案


                String output;
                //System.out.println("开始发送");
                while ((output = br.readLine()) != null) {
                    try {
                        JSONObject js = new JSONObject().parseObject(output.replace("data: ", ""));
                        if (js != null) {
                            String text = js.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").getString("content");
//                        //System.out.println(text);
                            if (text != null) {
//                                System.out.print(text);
                                sb.append(text);
//                            text.replaceAll("\n", "<br>");
//                            //System.out.println(text);
//                            writer.write("<span>"+text+"</span>"); //写出到网页
                                writer.write(text.replaceAll("\n", "{{END}}")); //写出到网页 换行替换成{{end}}
//                            writer.write("data:"+text); //写出到网页
//                            writer.write("\n\n"); //写出到网页
                                writer.flush();//发送操作

                            }
                        }
                    } catch (NullPointerException e) {
//                                    e.printStackTrace();
//                                    //System.out.println("空指针");
                    } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    //System.out.println("|>>json异常");
                    } catch (Exception e) {  //可能会出现解析的时候一点点事故（间隔发送的）但是没关系咱不管他
                        //System.out.println("发生错误");
                        e.printStackTrace();
                        //System.out.println("|>>>程序出现异常|-yc001:>>:" + e.getMessage());

                    }
                }

                String content = sb.toString();
                if(content.length()<1){ // 如果回答的字数小于1个那么就出现问题了就重新访问
                    //System.out.println("回答字数少于1位");
                    sum_sum_sum++;

                    if (sum_sum_sum == 5) {    // 如果失误5次就停止
                        //System.out.println("|||》》》》终止");
                        return new G2_SonModel("429", "no");
                    }
                    continue;
                }

                //System.out.println("发送消息成功");

                GptStaticModel.getList.get(gptKeyModel.key_id).key_id_sum=0; //如果当前密钥访问是成功的话那么重新回归到0次
                return new G2_SonModel("assistant",content);
            } catch (Exception e) {
                //System.out.println("|||》》》》进行重复查找");
//                    应该发出警报有密钥不正确了
                removeKey(gptKeyModel); //删除密钥


                e.printStackTrace();
                sum_sum_sum++;
                if (sum_sum_sum == 5) {    // 如果失误5次就停止
                    //System.out.println("|||》》》》终止");
                    return new G2_SonModel("429", "no");
                }
            }


        }

    }

















    /**返回一个密钥用于操作  密钥轮换*/
    public GptBalanceModel retrunKey(){

        int keyPresent = GptStaticModel.keyPresent;
        int keySum = GptStaticModel.keySum;

        if(keyPresent==0){
            //System.out.println("密钥数量为0个请注意密钥！！");
            return null;
        }
        int keyLong=keyPresent%keySum;//计算出当前是第多少个

        GptStaticModel.keyPresent++;

        GptBalanceModel gptBalanceModel = GptStaticModel.getList.get(keyLong);//拿到第n个密钥
//        gptBalanceModel.setId(keyLong); //如果密钥出事情 用于删除此id对应的密钥
        gptBalanceModel.key_id=keyLong; //拿到当前key

        return gptBalanceModel;
    }

//    如果密钥出现问题了删除密钥 并且设置当前密钥为异常
    public void  removeKey(GptBalanceModel key){
//        GptStaticModel.getList.get(key.key_id).key_id_sum++; //当前失败的次数加1

        GptBalanceModel gptBalanceModel1 = GptStaticModel.getList.get(key.key_id);//拿到当前
        gptBalanceModel1.key_id_sum++;
        //System.out.println("当前访问不了的次数为："+gptBalanceModel1.key_id_sum);
        //System.out.println(gptBalanceModel1);


        if(gptBalanceModel1.key_id_sum<6){ //如果失败的次数小于6次
            return;  //那么就不管他
        }


        //  数据库删除
        key.setStart("余额不足");
        Integer integer = iGptDao.update_gpt_key(key);
        //System.out.println("删除状态:"+integer);


        //当前区域删除
        boolean remove = GptStaticModel.getList.remove(key);//删除此密钥
        //System.out.println("list删除状态"+remove);
        if(remove==true){
            GptStaticModel.keySum--;
        }

        //System.out.println("当前删除的密钥为:"+key.getKey());

        for (GptBalanceModel gptBalanceModel : GptStaticModel.getList) {
            //System.out.println(gptBalanceModel.getKey());
        }



    }







//    批量插入
    public List<GptBalanceModel> insert_gpt_key(String  gptKey,String skOrSess){ //更具sk还是sess

//      更具正则表达式生成密钥
        Pattern pattern=null;
        if(skOrSess.equals("sk")){
            pattern = Pattern.compile("sk-[A-Za-z0-9]+");
        }else{
            pattern = Pattern.compile("sess-[A-Za-z0-9]+");
        }
        Matcher matcher = pattern.matcher(gptKey);

        List<GptBalanceModel> getList=new ArrayList<>();


        while (matcher.find()) {
            GptBalanceModel gpt=new GptBalanceModel();
            gpt.setKey(matcher.group()); //拿到对应的key
            gpt.setBalance("待添加");
            gpt.setSum("待添加");
            gpt.setUse("待添加");
            gpt.setStart("启用");
            gpt.setDate(UtioY.Date_getDate());
            getList.add(gpt);
        }
        //System.out.println(getList);

        Integer integer = iGptDao.insert_gpt_key(getList);
        if(integer==1){

            return getList;
        }

        return null;
    }


//    更新所有启用的余下的前
    public  List<GptBalanceModel> from_gpt_keys(){

        // 查询所有启用的
        GptBalanceModel gptModel = new GptBalanceModel();
        gptModel.setStart("启用");
        List<GptBalanceModel> gptBalanceModels = iGptDao.from_gpt_keys(gptModel); //查询
        List<GptBalanceModel> gptBalanceModels1 = gptFromBalancesList(gptBalanceModels); //更新集合里面的使用金额<使用的是余下的钱更新操作>
        Integer integer = iGptDao.update_gpt_keys(gptBalanceModels1); //更新数据库里面的内容



        if(integer==1){
            //更新gpt的可以使用密钥缓存
            List<GptBalanceModel> gptBalanceModels2 = iGptDao.from_gpt_keys(new GptBalanceModel());//返回查询的代码值
//            拿到所有为启用的名称
            GptStaticModel.getList = gptBalanceModels2.stream()
                    .filter(model -> model.getStart().equals("启用")) //根据start属性为启用筛选
                    .collect(Collectors.toList()); //将符合条件的元素收集到List中
            GptStaticModel.keySum=GptStaticModel.getList.size(); //保存到有多少个key的数量
//            //System.out.println("更新后的值："+ GptStaticModel.getList);
            return   gptBalanceModels2;
        }

//        如果更新失败了
        return null;


    }



    //    更新所有启用的余下的前
    public  List<GptBalanceModel> from_gpt_keys_yc(){

        // 查询所有启用的
        GptBalanceModel gptModel = new GptBalanceModel();
        gptModel.setStart("启用");
        List<GptBalanceModel> gptBalanceModels = iGptDao.from_gpt_keys(gptModel); //查询
        Integer integer = iGptDao.update_gpt_keys(gptBalanceModels); //更新数据库里面的内容



        if(integer==1){
            //更新gpt的可以使用密钥缓存
            List<GptBalanceModel> gptBalanceModels2 = iGptDao.from_gpt_keys(new GptBalanceModel());//返回查询的代码值
//            拿到所有为启用的名称
            GptStaticModel.getList = gptBalanceModels2.stream()
                    .filter(model -> model.getStart().equals("启用")) //根据start属性为启用筛选
                    .collect(Collectors.toList()); //将符合条件的元素收集到List中
            GptStaticModel.keySum=GptStaticModel.getList.size(); //保存到有多少个key的数量
//            //System.out.println("更新后的值："+ GptStaticModel.getList);
            return   gptBalanceModels2;
        }

//        如果更新失败了
        return null;


    }


    //多条记录直接查询
    public List<GptBalanceModel> gptFromBalancesList(List<GptBalanceModel> balanceList) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); //设置线程池线程池大小用当前系统能给的大小来代替

        for (GptBalanceModel gptBalance : balanceList) {//进行for循环查询遍历
            executor.submit(() -> getFromBalance(gptBalance));
        }

        executor.shutdown(); //等待线程执行完成
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return balanceList; //返回数据
    }


//    一个一个查询
    private GptBalanceModel getFromBalance(GptBalanceModel gptBalance){

        String url = gptUrl+"/v1/dashboard/billing/subscription";
        String apiKey = gptBalance.getKey(); //设置秘钥
        try {
//            拿到当前剩余余下的钱
            Document doc = Jsoup.connect(url)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("Connection", "keep-alive")
                    .ignoreContentType(true)
                    .get();


//            查找当前已经使用多少
            String startDate = LocalDate.now().minusDays(99).toString();
            String endDate = LocalDate.now().plusDays(1).toString();
            String billingUrl = gptUrl+"/v1/dashboard/billing/usage?start_date=" + startDate + "&end_date=" + endDate;

            Document billingDoc = Jsoup.connect(billingUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("Connection", "keep-alive")
                    .ignoreContentType(true)
                    .get();

//                拿到一共剩余
            String html = doc.body().text(); // 获取HTML代码
            JSONObject json =JSONObject.parseObject(html); // 将HTML代码转换为JSON对象
            Double hardLimitUsd = Double.parseDouble(json.getString("hard_limit_usd")); // 获取hard_limit_usd的值


//            拿到已经使用的
            String html1 = billingDoc.body().text(); // 获取HTML代码
            JSONObject json1 = (JSONObject) JSONObject.parse(html1); // 将HTML代码转换为JSON对象
            Double total_usage = Double.parseDouble(json1.getString("total_usage")) / 100.0;


//            保留两位小数
            String sum = String.format("%.3f", hardLimitUsd);
            String use      = String.format("%.3f", total_usage);
            String balance= String.format("%.3f", hardLimitUsd-total_usage);
//            //System.out.println("一共: " + sum);
//            //System.out.println("已使用:"+use);
//            //System.out.println("剩余金额:"+balance);

//            如果剩下的钱小于0的话
            if(hardLimitUsd-total_usage<0){
                gptBalance.setStart("余额不足");
            }

            gptBalance.setSum(sum);//一共
            gptBalance.setUse(use);//已使用
            gptBalance.setBalance(balance);//剩余金额


            return gptBalance;


        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("余额更新失败");
            gptBalance.setStart("key异常");
            return gptBalance;
        }
    }

}





