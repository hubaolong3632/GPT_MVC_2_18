package com.chatgpt.z_ljx;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WebsiteLoadTester {
    private static final String URL = "http://00000.work:19099/login/loginNamePassword?name=name&password=pasword";
    private static final int NUM_REQUESTS = 500; // 并发请求的数量

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_REQUESTS);

        for (int i = 0; i < NUM_REQUESTS; i++) {
            executorService.execute(() -> sendRequest());
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    private static void sendRequest() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(URL);
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Status code: " + statusCode);
        } catch (IOException e) {
            System.out.println("请求发生错误：" + e.getMessage());
        }
    }
}