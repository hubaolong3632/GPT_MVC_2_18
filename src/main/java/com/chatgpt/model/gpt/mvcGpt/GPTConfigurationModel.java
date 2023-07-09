package com.chatgpt.model.gpt.mvcGpt;

import com.chatgpt.model.gpt.webGpt.G2_SonModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor  //无参数全部构造器
//@AllArgsConstructor  //有参全构造器
public class GPTConfigurationModel {
     public String model ; //模型类型
     public List<G2_SonModel> messages;
     public double temperature; //温度
     public  boolean stream;// 流


}
