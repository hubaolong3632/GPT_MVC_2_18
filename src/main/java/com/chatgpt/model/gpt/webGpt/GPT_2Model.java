
package com.chatgpt.model.gpt.webGpt;
import java.util.List;

public class GPT_2Model {
     public String model ;
     public List<G2_SonModel> messages;
     public long sum=0;
     public GPT_2Model(String model, List<G2_SonModel> messages) {
          this.model = model;
          this.messages = messages;
          sum++; //记录用户访问的次数
     }


}
