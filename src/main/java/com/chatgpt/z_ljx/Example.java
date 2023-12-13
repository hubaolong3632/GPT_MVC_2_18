//package com.chatgpt.z_ljx;
//
//import com.chatgpt.utio.UtioY;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class Example {
//    public static void main(String[] args) {
//        try {
//            String s = UtioY.Random_number20();
//            System.out.println(s);
//
//            Document doc = Jsoup.connect(
//                    "http://43.142.4.250:8018/book/list/1/10"
//            ).ignoreContentType(true).post(); //忽略请求体
//            doc.body
//
//            System.out.println(doc.text());
//
//
//
//            // 在这里处理返回的 JSON 数据
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}