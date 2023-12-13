package com.chatgpt.z_ljx;

import com.chatgpt.utio.UtioY;
import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ces1 {
    @SneakyThrows
    public static void main(String[] args) {
//        String json = generateLargeJson(); // 生成大量的JSON数据
//        Document doc = Jsoup.connect("http://xn--g5tp58j.life:8080/register")
//        String json = "{\"message\":\"1\",\"com\":99}";
//        Document doc = Jsoup.connect("http://00000.work:19099/gpt/gptUpdate11")
//                .header("Content-Type", "application/json")
//                .requestBody(json)
//                .post();
//        System.out.println(doc.body());
        String s = UtioY.Random_number20();
        System.out.println(s);
//        String json = "{\"password\":\"123\",\"username\":\"123\"}";
//        Document doc = Jsoup.connect("http://xn--g5tp58j.life/user/login")
        Document doc = Jsoup.connect("\n" +
                        "http://www.xbhp.cn/news/150723.html")
                .header("Content-Type", "application/json")
//                .requestBody(json)
//                .post();
                .get();
        System.out.println(doc);


    }
}
