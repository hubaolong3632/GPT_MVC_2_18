package com.chatgpt.idao;


import com.chatgpt.model.gpt.mvcGpt.GptBalanceModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//GPT接口
@Repository
public interface IGptDao {


   /**修改数据里面的内容 （如密钥  <-->  当前状态 （启动还是停止））*/
    public Integer update_gpt_key(@Param("gpt")GptBalanceModel gpt);

   /**修改数据里面的内容多条修改 （如密钥  <-->  当前状态 （启动还是停止））*/
   public Integer update_gpt_keys(@Param("gptList") List<GptBalanceModel> gptList);

    /**查询所有的密钥  或者更具指定条件查询*/
    public List<GptBalanceModel> from_gpt_keys(@Param("gpt")GptBalanceModel gpt);

    /**插入一条密钥*/
    public Integer insert_gpt_key(@Param("gptList")List<GptBalanceModel> gptList);


    /***/

    /***/


    /***/

}
