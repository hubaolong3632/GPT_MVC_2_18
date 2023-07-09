package com.chatgpt.model.gpt.mvcGpt;

import lombok.Data;

@Data
public class GptNumberModel {
  public   Long numberIp;
  public   Long numberUser;
  public   Boolean sumBol;

    public GptNumberModel() {
    }

    public GptNumberModel(Long numberIp, Long numberUser, Boolean sumBol) {
        this.numberIp = numberIp;
        this.numberUser = numberUser;
        this.sumBol = sumBol;
    }
}
