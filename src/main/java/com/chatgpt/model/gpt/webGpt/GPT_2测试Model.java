package com.chatgpt.model.gpt.webGpt;
import java.util.List;

public class GPT_2测试Model {
     public String model ;
     public List<G2_SonModel> messages;
     public double temperature; //温度
     public  boolean stream;// 流

     public GPT_2测试Model(String model, List<G2_SonModel> messages, double temperature, boolean stream) {
          this.model = model;
          this.messages = messages;
          this.temperature = temperature;
          this.stream = stream;
     }
}
