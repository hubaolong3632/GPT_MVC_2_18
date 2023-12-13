package com.chatgpt.z_ljx;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class ChatClient extends WebSocketListener {

    private final OkHttpClient client;
    private WebSocket webSocket;

    public ChatClient() {
        this.client = new OkHttpClient();
    }

    public void start() {
        // 获取 conversationId, conversationSignature, clientId
        String cookies = "_EDGE_V=1; MUID=38CD4F159A87637B31BC5C239BCD620C; MUIDB=38CD4F159A87637B31BC5C239BCD620C; SRCHD=AF=NOFORM; SRCHUID=V=2&GUID=F39FF0392EBD4CD986ECA1049665C34B&dmnchg=1; ANON=A=6EF45FC06DEE4CBE9DE3C5EDFFFFFFFF; _UR=QS=0&TQS=0; SnrOvr=X=rebateson; _clck=c97mlc|2|fcm|0|1266; USRLOC=HS=1&ELOC=LAT=37.39393997192383|LON=-121.96007537841797|N=%E5%9C%A3%E5%85%8B%E6%8B%89%E6%8B%89%EF%BC%8C%E5%8A%A0%E5%88%A9%E7%A6%8F%E5%B0%BC%E4%BA%9A|ELT=1|; ANIMIA=FRE=1; MMCASM=ID=88982758A9754AD5AF5E084D17078E2C; _EDGE_S=SID=0D1E50A1382860393E0B43EB39FA61B7; WLS=C=18309e71c810071e&N=%e4%b8%80; SUID=A; SNRHOP=I=&TS=; _U=1bq6c0HnY-Tka6-1Iz8okPv-iqUMvDfDD7srWaJU8HNOirrfTzgWpLb9huX_jWVoAuMn4oXFQkZGZxE6VcWjCZB4OHvVqC4umGY9g8RVBLk4I-4T3B0sc4gmxaYiKFCIPPbmqDBIM1uCH4k6wsmlHuUrU0C4mGJxGEPXTOryFdiX2NCWCOgOnsQVFEb1ltym9YUpwnCXjndMZXFVHciS49HtpVl7IRY2yi7V4LA1zULI; _HPVN=CS=eyJQbiI6eyJDbiI6MiwiU3QiOjAsIlFzIjowLCJQcm9kIjoiUCJ9LCJTYyI6eyJDbiI6MiwiU3QiOjAsIlFzIjowLCJQcm9kIjoiSCJ9LCJReiI6eyJDbiI6MiwiU3QiOjAsIlFzIjowLCJQcm9kIjoiVCJ9LCJBcCI6dHJ1ZSwiTXV0ZSI6dHJ1ZSwiTGFkIjoiMjAyMy0wNy0xMVQwMDowMDowMFoiLCJJb3RkIjowLCJHd2IiOjAsIkRmdCI6bnVsbCwiTXZzIjowLCJGbHQiOjAsIkltcCI6NH0=; SRCHUSR=DOB=20230620&T=1689090209000; ipv6=hit=1689093824548&t=4; GC=kC_hP70oe57JHkGokT8qPd5-rV9r_4oVoBFmW--AK-mAuBxqkCg33K6CDaLH-79pv6AyVvpxXdqUNij06fHYAg; _RwBf=r=1&mta=0&rc=190&rb=190&gb=0&rg=0&pc=190&mtu=0&rbb=0.0&g=0&cid=&clo=0&v=2&l=2023-07-11T07:00:00.0000000Z&lft=0001-01-01T00:00:00.0000000&aof=0&o=0&p=bingcopilotwaitlist&c=MY00IA&t=8322&s=2023-03-16T06:14:56.5726686+00:00&ts=2023-07-11T15:58:17.9131730+00:00&rwred=0&wls=2&lka=0&lkt=0&TH=&e=XYIqx9gZymW0aDPxp6fxDLaK07A-9WiF7RRndGSjb84ROcy333UvPOQeylp-85NGfRiujkNZrn50e-iIh7r1Dg&A=&dci=0; _SS=SID=0D1E50A1382860393E0B43EB39FA61B7&R=190&RB=190&GB=0&RG=0&RP=190; SRCHHPGUSR=SRCHLANG=zh-Hans&PV=6.0&BRW=N&BRH=T&CW=1165&CH=1799&SCW=1164&SCH=4202&DPR=2.0&UTC=480&DM=1&HV=1689090223&PRVCW=1165&PRVCH=1804&EXLTT=3&TH=ThAb1";
        String conversationId = "";
        String clientId = "";
        String conversationSignature = "";

        try {
            Request request = new Request.Builder()
                    .url("https://www.bing.com/turing/conversation/create")
                    .header("Cookie", cookies)
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            JSONObject jsonData = new JSONObject(responseData);
            conversationId = jsonData.getString("conversationId");
            clientId = jsonData.getString("clientId");
            conversationSignature = jsonData.getString("conversationSignature");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 建立 WebSocket 连接
        Request wsRequest = new Request.Builder()
                .url("wss://sydney.bing.com/sydney/ChatHub")
                .build();
        webSocket = client.newWebSocket(wsRequest, this);

        // 发送初始请求
        String initialRequest = "{\"protocol\":\"json\",\"version\":1}";
        webSocket.send(initialRequest);

        String secondRequest = "{\"type\":6}";
        webSocket.send(secondRequest);

        // 构建发送的数据
        JSONObject data = new JSONObject();
        JSONObject message = new JSONObject();
        message.put("text", "要问的问题");
        message.put("messageType", "Chat");

        data.put("arguments", new JSONObject[]{
                new JSONObject()
                        .put("source", "cib")
                        .put("isStartOfSession", true)
                        .put("message", message)
                        .put("conversationSignature", conversationSignature)
                        .put("participant", new JSONObject()
                                .put("id", clientId))
                        .put("conversationId", conversationId)
        });
        data.put("invocationId", "1");
        data.put("target", "chat");
        data.put("type", 4);

        webSocket.send(data.toString());
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        // WebSocket 连接成功
        System.out.println("WebSocket 连接成功");
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        // 接收到文本消息
        System.out.println("接收到消息：" + text);

        JSONObject msgData = new JSONObject(text);
        int type = msgData.getInt("type");
        switch (type) {
            case 1:
                // 文本消息
                String message = msgData.getJSONArray("arguments").getJSONObject(0)
                        .getJSONArray("messages").getJSONObject(0).getString("text");
                System.out.println("回答：" + message);
                break;
            case 2:
                // 结束会话
                System.out.println("会话结束");
                webSocket.close(1000, "会话结束");
                break;
        }
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
        // WebSocket 连接关闭中
        System.out.println("WebSocket 连接关闭中：" + reason);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        // WebSocket 连接已关闭
        System.out.println("WebSocket 连接已关闭：" + reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        // 连接失败
        System.out.println("WebSocket 连接失败：" + t.getMessage());
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }
}