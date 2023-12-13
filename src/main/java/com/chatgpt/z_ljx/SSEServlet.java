//package com.chatgpt.z_ljx;
//
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONException;
//import com.alibaba.fastjson.JSONObject;
//import com.chatgpt.model.gpt.webGpt.G2_SonModel;
//import com.chatgpt.model.gpt.webGpt.GPT_2测试Model;
//import com.chatgpt.model.gpt.webGpt.GptUser;
//import com.chatgpt.utio.UtioClass.DateUtio;
//import com.chatgpt.utio.UtioClass.IP;
//import com.chatgpt.utio.UtioClass.JS;
//import com.chatgpt.utio.UtioY;
//
//import javax.net.ssl.HttpsURLConnection;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.swing.*;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.Timer;
//
//
////采用流视发送
//@WebServlet(asyncSupported = false, urlPatterns = { "/index" })
//public class SSEServlet extends HttpServlet {
//
//    private static final long serialVersionUID = 1L;
//
//    private   static   List<String> list = new ArrayList<>(); // 待优化：使用List更好地管理API密钥
//    private   static   List<String> ip_no = new ArrayList<>(); // 用于记录用户黑名单
//    private static   int sum_key = 0; // key 的交换
//    public   static  Map<String, GptUser> mapUser=new HashMap<>(); //保存用户访问信息
//
//    public  static   long USER_sum_visit=0; // 记录用户访问数量
//    public  static  long API_sum_visit=0; // 记录今日访问量
//
//    public static Map<String,Integer> IPSumMap=new HashMap<>(); //统计各个ip 访问次数
//
////    private  static  Map<String, GptUser> mapUser=new HashMap<>(); //保存用户访问信息
//
//    public static List<String> listURL=new ArrayList<>();
//    public static int sum_URL=0; //统计URL的
//
////    获取配置文件初始化等操作
//    static {
//        System.out.println("GPT加载成功!<0.3>");
//
//    listURL.add("https://openai.1rmb.tk/v1/chat/completions");
//    listURL.add("https://openai.1rmb.tk/v1/chat/completions");
//    listURL.add("https://openai.1rmb.tk/v1/chat/completions");
//
////    listURL.add("https://vercel.askopenai.tech/v1/chat/completions");
////    listURL.add("https://api.askgptai.tech/v1/chat/completions");
////    listURL.add("https://api.askgptai.tech/v1/chat/completions");
////    listURL.add("https://api.askgptai.tech/v1/chat/completions");
//
//
////官网的
////    listURL.add("https://api.openai.com/v1/chat/completions");
////    listURL.add("https://api.openai.com/v1/chat/completions");
////    listURL.add("https://api.openai.com/v1/chat/completions");
//
//
//
////    listURL.add("https://openai.451024.xyz/v1/chat/completions");
////    listURL.add("https://openai.451024.xyz/v1/chat/completions");
////    listURL.add("https://openai.451024.xyz/v1/chat/completions");
//
//
//        try {
//            //获取配置文件
//            ResourceBundle pzwj = ResourceBundle.getBundle("PZWJ", new ResourceBundle.Control() {
//                @Override
//                public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {  //获取配置文件
//                    String encoding = "UTF-8";// 定义文件编码
//                    InputStream is = loader.getResourceAsStream(toBundleName(baseName, locale) + ".properties");
//                    return new PropertyResourceBundle(new InputStreamReader(is, encoding));
//                }
//            });
//
//
//            //获取所有密钥
//            for (Object key_m : JSONObject.parseObject(pzwj.getString("key_value")).getJSONArray("value")) { //获取保存起来的PXEJ为key_value字段 中的json格式中的value属性
//                list.add((String) key_m);//获取密钥
//            }
//            System.out.println("密钥数量："+list.size());
//
//
//
//            for (Object key_m : JSONObject.parseObject(pzwj.getString("ip_no")).getJSONArray("value")) { //获取保存起来的PXEJ为key_value字段 中的json格式中的value属性
//                System.out.println("黑名单用户:"+key_m);
//                ip_no.add((String) key_m);//获取密钥
//            }
//
//
//
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//
//
//
//    }
//
//    @Override
//    public void init() throws ServletException {
//        super.init();
//
////        try {
////            // 获取当前目录
////            String currentPath = System.getProperty("user.dir");
////
////            LocalDateTime currentTime = LocalDateTime.now();
////            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm-ss");
////            System.out.println(currentTime.format(formatter));
////            File outputFile = new File(currentPath + "/" + currentTime.format(formatter) + "_log.txt");// 创建输出文件对象
////
////            if (!outputFile.exists()) {// 如果文件不存在，则创建文件
////                System.out.println("创建文件");
////                outputFile.createNewFile();
////            }
////
////            FileOutputStream fos = new FileOutputStream(outputFile, true); // 创建文件输出流
////            System.out.println(outputFile);
////            PrintStream ps = new PrintStream(fos); // 创建PrintStream，设置输出流为文件输出流
////
////            System.setOut(ps); // 将System.out重定向到文件输出流
////
////
////
////
////
//            Timer timer = new Timer();
//            // 获取当前时间
//            Calendar calendar = Calendar.getInstance();
//            Date now = calendar.getTime();
//            // 计算第一次执行任务的时间，明天的0点
//            calendar.set(Calendar.HOUR_OF_DAY, 0);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 0);
//            calendar.set(Calendar.MILLISECOND, 0);
//            Date firstTime = calendar.getTime();
//            // 如果当前已经过了0点，则第一次执行时间为明天
//            if (now.after(firstTime)) {
//                calendar.add(Calendar.DATE, 1);
//                firstTime = calendar.getTime();
//            }
//            // 执行任务
//            timer.schedule(new ZeroClockTaskThread(), firstTime, 24 * 60 * 60 * 1000);
//
////
////
////
////
////
////            System.out.println("当前系统时间：" + new Date()); // 输出内容
////        }catch (Exception e){
////            e.printStackTrace();
////            System.out.println("目录保存失败文件路径错误");
////        }
////
////
////
//
//
//
//
//
//
//
//
//
//
//
//
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String userIp = UtioY.IP_getIp(request);//访问ip的人的访问
//
//
//        try {
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            //设置传输格式
//            response.setContentType("text/event-stream");
//            response.setCharacterEncoding("UTF-8");
//            response.flushBuffer();
//            PrintWriter writer = response.getWriter();
//
//
//        // 黑名单
//            if(ip_no.contains(userIp)){
//                System.out.println("黑名单用户访问");
//                writer.write("你的账号在黑名单中 ,请联系qq: 911412667 进行解除封禁"); //写出到网页
//                writer.flush();//发送操作
//                writer.close(); //关闭流操作
//                return ;
//
//            }
//
//
//            //解析传输过来的数据            读取请求的数据并解析为 JSON 对象
//            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
//            String json_S1 = reader.readLine();
////            System.out.println("打印请求值:"+json); //值
//            JSONObject js1=new JSONObject().parseObject(json_S1);
//
//            String message = js1.getString("message"); //消息
//            String userName = js1.getString("user"); //访问用户名称
//
//            List<G2_SonModel> listGPT=null;
//
//
//
//             int sum_sum_sum=0;
//
//
//                      System.out.println("\n-----------------------------------------------------");
//                        if(mapUser.containsKey(userName)){ //存在的时候
//                            System.out.println("|001>>:存在用户");
//                            listGPT = mapUser.get(userName).getMessage(); //获取集合
//                            System.out.println("|003>>:访问数量:"+(listGPT.size()+1)); //加1因为从0开始
//
//                            if (listGPT.size()>10) { //连续模式下最多10条
//                                mapUser.get(userName).setMessage(new ArrayList<>()); //重新创建一个如果超过15条
//                                System.out.println("|111>>:超过10条已经重新开始");
//                            }
//                            listGPT.add(new G2_SonModel("user", message));
//
//                        }else{
//                            System.out.println("|002>>:新用户访问");
//                            //创建消息集合
//                            listGPT = new ArrayList<>();
//                            listGPT.add(new G2_SonModel("user", message));
//                            GptUser gptUser = new GptUser(listGPT);
//                            mapUser.put(userName,gptUser);// 以名字带对象的方法使用
//
//                            USER_sum_visit++; //今日访问人数
//                        }
//                         System.out.println("msg:"+message);
//                            API_sum_visit++; //今日访问量
//
//                      System.out.println("|--今日访问人数:"+USER_sum_visit+"   今日访问数量:"+API_sum_visit+"--|");
//
//
//
//                      if(!IPSumMap.containsKey(userIp)){ //如果是第一次进来
//                          IPSumMap.put(userIp,1);
//                          System.out.println("新的ip进入:"+userIp);
//
//                      }else{
//                          int ipSum = IPSumMap.get(userIp) + 1;
//                          IPSumMap.put(userIp,ipSum); //对于当前ip数量加1
//                          System.out.println("当前访问次数:"+ipSum);
//
//
//                          if(ipSum>=300){
//                              System.out.println("次数大于100次");
//                              writer.write("当前访问次数:"+ipSum+"  >当前ip访问次数过高(本软件可以免费使用但是不允许恶意访问 如果是没有请求到100次就封禁了可能是同ip有人使用,)\n请联系:  qq:911412667  进行解除访问次数  除了非正常访问都可以解除限制  你所使用的ip为"+userIp); //写出到网页
//                              writer.flush();//发送操作
//                              writer.close(); //关闭流操作
//                              return ;
//                          }
//                      }
//
//
//
//                      while(true){
//                          try {
////                            GPT_2测试Model g2 = new GPT_2测试Model("gpt-3.5-turbo", listGPT, 0.9, true);
//                            GPT_2测试Model g2 = new GPT_2测试Model("gpt-3.5-turbo-16k", listGPT, 0.9, true);
////                            GPT_2测试Model g2 = new GPT_2测试Model("gpt-3.5-turbo-0613", listGPT, 0.9, true);
//                            String json = JS.JSONNoDate(g2); //输出测试用例 并且不带时间
//
//
//                            byte[] postData = json.getBytes("UTF-8");
//
//                               sum_URL=sum_URL%3;
//                              String url= listURL.get(sum_URL);
//                              System.out.println("当前使用的URLID:"+sum_URL+"      为"+url);
//                              sum_URL++;
//
////                            URL serverUrl = new URL(url);
////                            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
//
//                              URL serverUrl = new URL(url);
//                              HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
//                           conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//
//                            conn.setRequestMethod("POST");
//                            conn.setDoOutput(true);
//                            conn.setRequestProperty("Content-Type", "application/json");
//
//
//                            //进行连接GPT官网
//                            System.out.println("第:"+sum_key % list.size()+"     |007>> 使用的秘钥是:"+list.get(sum_key % list.size()));
//                            conn.setRequestProperty("Authorization", "Bearer " + list.get(sum_key % list.size()) + "");
//                            sum_key++;
//                            conn.setRequestProperty("Connection", "Keep-Alive"); //持续连接
//                            conn.setConnectTimeout(200000); //时长
//                            OutputStream wr = conn.getOutputStream();
//                            wr.write(postData);
//                            wr.flush();
//
//
//
//
//
//                            //收到的数据发送给前端
//                            InputStream is = conn.getInputStream();
//                            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                            String output;
//                            while ((output = br.readLine()) != null) {
//
//                                try {
//                                    JSONObject js = new JSONObject().parseObject(output.replace("data: ", ""));
//                                    if (js != null) {
//                                        String text = js.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").getString("content").replaceAll("\n", "{{END}}");
//                                        if (text != null) {
//                                            System.out.print(text);
//                                            writer.write(text); //写出到网页
//                                            writer.flush();//发送操作
//
//                                        }
//                                    }
////                                }catch (NullPointerException e){
////                                    e.printStackTrace();
////                                    System.out.println("空指针");
//                                }catch (JSONException e){
////                                    e.printStackTrace();
////                                    System.out.println("|>>json异常");
//                                }
//                                catch (Exception e) {  //可能会出现解析的时候一点点事故（间隔发送的）但是没关系咱不管他
//                                    System.out.println("发生错误");
//                                    e.printStackTrace();
//                                    System.out.println("|>>>程序出现异常|-yc001:>>:"+e.getMessage());
//
//                                }
//                            }
//
//
//
//                      System.out.println("|ip>>:::"+ userIp);
//                      System.out.println("|date>>:::"+ DateUtio.dateDay());
//                      System.out.println("|100---->《发送成功》");
//
//                      break;
//                  }catch (Exception e){  //出现其他问题
//
////                      如果失误5次就停止
//                        sum_sum_sum++;
//                      if(   sum_sum_sum   ==5){
//                          System.out.println("终止");
//                          return;
//                      }
//                              System.out.println("次数加1"+      sum_sum_sum      );
//
//                      System.out.println("|>>>程序出现异常|-yc002:>>:"+e.getMessage());
//
//                      e.printStackTrace();
//                 }
//                        }
//                writer.close(); //关闭流操作
//
//
//        }catch (Exception e){ //连接的时候突然客户端关闭
//            System.out.println("|>>>程序出现异常|-yc003:>>:"+e.getMessage());
//
//            e.printStackTrace();
//        }
//
//    }
//
//
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    }
//
//
////    每天自动启动的代码
//    private class ZeroClockTaskThread extends TimerTask {
//        @Override
//        public void run() {
//            System.out.println("-------------执行每天的任务成功--------");
//            SSEServlet.IPSumMap=new HashMap<>(); //清理掉当天的访问量
//            SSEServlet.mapUser=new HashMap<>(); //清理所有用户访问
//            USER_sum_visit=0; // 记录用户访问数量
//            API_sum_visit=0; // 记录今日访问量
//
//        }
//    }
//}
//
//
//
