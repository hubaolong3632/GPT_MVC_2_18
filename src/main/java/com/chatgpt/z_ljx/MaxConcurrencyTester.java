package com.chatgpt.z_ljx;

import com.alibaba.fastjson.JSONObject;
import com.chatgpt.utio.UtioY;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MaxConcurrencyTester {

    private static String generateLargeJson() {
        JSONObject jsonObject = new JSONObject();

        // 生成大量的键值对
        for (int i = 0; i < 100000; i++) {
            jsonObject.put("key" + i, "value" + i);
        }
        return jsonObject.toString();
    }

    private static void sendRequest() {
        try {

//            String s = UtioY.Random_number20();
//            System.out.println("输出值"+s);

//            Document doc = Jsoup.connect(
//                    "https://app5608.acapp.acwing.com.cn/api/user/account/register/?username="+s +"&password=11111&confirmPassword=11111"
//            ).ignoreContentType(true).post(); //忽略请求体


//            Document doc = Jsoup.connect(
//                    "http://43.142.4.250:8018/book/list/1/10"
//            ).ignoreContentType(true).get(); //忽略请求体
//                .requestBody(json)
//            System.out.println("回应"+doc.text());




//            Document doc = Jsoup.connect("\n" +
//                            "        String url = \"https://app5608.acapp.acwing.com.cn/api/user/account/register/?username="+UtioY.Random_number20()+"&password=11111&confirmPassword=11111\";")
//                    .header("Content-Type", "application/json")
//                .requestBody(json)
//                .post();
//                    .post();



//            String json = "{\"name\":\"\"}";
//            Document doc = Jsoup.connect("http://43.142.4.250:8018/book/list/1/10")
//                    .header("Content-Type", "application/json")
//                    .requestBody(json)
//                    .ignoreContentType(true)
//                    .post();
//            System.out.println(doc);

//            Document doc = Jsoup.connect("https://www.sitesmo.com/site/wallet.html")
//                    .header("Cookie","Hm_lvt_b1c99468fe6b0e97a00b6bcbe50b248d=1700627723; PHPSESSID=pnufs3fs1f5aojnfu4n6090vca; x_sure__user_identity=37fa54e90fd46f56bd530eadc85b44c2910c20b4bf6b22428fb2cb55f17be3c2a%3A2%3A%7Bi%3A0%3Bs%3A21%3A%22x_sure__user_identity%22%3Bi%3A1%3Bs%3A49%3A%22%5B10082%2C%22rt-Ry3TSkH6MD4cSE3cWfN3AUqxdtsC9%22%2C604800%5D%22%3B%7D; _csrf=2b855d42b6ad3e37bb13057b68b2af8095deb61781280ac6b534d758a6304cbaa%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22z_PKvd-3nnWAIRA_-f4acX4c7JPLy407%22%3B%7D; Hm_lpvt_b1c99468fe6b0e97a00b6bcbe50b248d=1700628472")
//                    .header("X-Csrf-Token","4OZJALbLGuqf2U6E9HEQhjqeOii8LUMbr-NSoCPTUNyauRlLwK832fG3GcW9I1HZF_gOSd91d3iYqQLsWudg6w==")
//                    .data("type", "draw")  // 添加form-data参数
//                    .data("prompt", "333")
//                    .data("choose", "1024x1024")
//                    .data("choose_quality", "standard")
//                    .data("choose_style", "vivid")
//                    .ignoreContentType(true).post(); //忽略请求体
//            String jsonStr = doc.text();
//            System.out.println(jsonStr);

            Document doc = Jsoup.connect("https://www.mingshisanling.com/mausoleum.html?c_id=4")
                    .ignoreContentType(true).get(); //忽略请求体
            String jsonStr = doc.text();
            System.out.println(jsonStr);

//            JSONObject jsonObject = JSONObject.parseObject(jsonStr);

        } catch (Exception e) {
//            System.out.println("请求发生错误：" + e.getMessage());
        }
    }
    @Test
    public void abc() throws InterruptedException {
        while (true) {
            int maxThreads = 400; // 获取处理器核心数量
//        int maxThreads = 3; // 获取处理器核心数量
            ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);

            for (int i = 0; i < maxThreads; i++) {
                int finalI = i;
                executorService.execute(() -> {
                    // 这里放置你想做的任务
                    // 每个线程需要执行的代码
//                System.out.println("线程"+ finalI +"进行压力测试");
                    while (true) {
                        try {
                            sendRequest();

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
//                    System.out.println("cc");
                    }
                });
            }
            System.out.println("全部启动");

            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        }
    }

    @Test
    public void abc1() throws InterruptedException {
        while (true) {
            int maxThreads = 20000; // 获取处理器核心数量
//        int maxThreads = 3; // 获取处理器核心数量
            ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);

            for (int i = 0; i < maxThreads; i++) {
                int finalI = i;
                executorService.execute(() -> {
                    // 这里放置你想做的任务
                    // 每个线程需要执行的代码
//                System.out.println("线程"+ finalI +"进行压力测试");
                    while (true) {
                        sendRequest();
//                    System.out.println("cc");
                    }
                });
            }
            System.out.println("全部启动");

            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        }
    }

    @Test
    public void abc2() throws InterruptedException {
        while (true) {
            int maxThreads = 20000; // 获取处理器核心数量
//        int maxThreads = 3; // 获取处理器核心数量
            ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);

            for (int i = 0; i < maxThreads; i++) {
                int finalI = i;
                executorService.execute(() -> {
                    // 这里放置你想做的任务
                    // 每个线程需要执行的代码
//                System.out.println("线程"+ finalI +"进行压力测试");
                    while (true) {
                        sendRequest();
//                    System.out.println("cc");
                    }
                });
            }
            System.out.println("全部启动");

            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
//        int maxThreads = Runtime.getRuntime().availableProcessors(); // 获取处理器核心数量
//        int maxThreads = 6000; // 获取处理器核心数量
//        int maxThreads = 10000; // 获取处理器核心数量
            int maxThreads = 20000; // 获取处理器核心数量
//        int maxThreads = 3; // 获取处理器核心数量
            ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);

            for (int i = 0; i < maxThreads; i++) {
                int finalI = i;
                executorService.execute(() -> {
                    // 这里放置你想做的任务
                    // 每个线程需要执行的代码
//                System.out.println("线程"+ finalI +"进行压力测试");
                    while (true) {
                        sendRequest();
//                    System.out.println("cc");
                    }
                });
            }
            System.out.println("全部启动");

            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        }
    }
}