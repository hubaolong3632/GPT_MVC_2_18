package com.chatgpt.a_controller;

import com.chatgpt.model.PersonRecord;
import com.chatgpt.model.gpt.mvcGpt.GptBalanceModel;
import com.chatgpt.model.gpt.mvcGpt.GptMessageModel;
import com.chatgpt.service.GptService;
import com.chatgpt.utio.UtioCode.Result;
import com.chatgpt.utio.UtioCode.ResultCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/gpt")
//GPT接口
@PropertySource("classpath:configuration.properties") //配置文件注入
public class ChatGPTController {
    @Value("${gpt.urlMessage}")
    private   String url; //反向代理接口

    @Resource
    private     GptService gptService;




    //   暂停消息
    @RequestMapping("/no")
    @ResponseBody
    public Result no(){
        System.out.println("执行暂停消息");
        return Result.failure(ResultCode.No_SUCCESS,"暂停成功");
    }




    //    插入密钥
    @RequestMapping("/cs")
    @ResponseBody
//    public Result cs(@RequestBody JSONObject json){
    public Result cs(@RequestBody PersonRecord json){

        System.out.println(json);

        return Result.success("成功");
    }






    //    插入密钥
    @RequestMapping("/insert")
    @ResponseBody
    public Result insert_gpt_key(String gptKey){

        try{

        List<GptBalanceModel> gptBalanceModels = gptService.insert_gpt_key(gptKey);
        if(gptBalanceModels==null){
            return Result.failure(ResultCode.No_SUCCESS, "数据库插入失败");
        }else{
            return Result.success(gptBalanceModels);//数据库值插入成功
        }
        }catch (Exception e){

            return Result.Not___SYSTEM_INNER_ERROR(e);
        }


    }



//    gpt里面的数据更新代码
    @RequestMapping("/update")
    @ResponseBody
    public Result gptUpdate() { //更新gpt里面的余钱等等
        try {
                 return Result.success(gptService.from_gpt_keys()); //数据库更新等等

        } catch (Exception e) {
            e.printStackTrace();
            return Result.Not___SYSTEM_INNER_ERROR(e.getMessage());
        }
    }

    /**流输出gpt*/
    @RequestMapping("/gptStream")
    public void gptStream(@RequestBody GptMessageModel message, HttpServletRequest request, HttpServletResponse response, @RequestHeader("Authorization") String jwt){
        System.out.println(message);
        try{

        //   理论来说会返回消息内容和回答的内容  用于保存数据库
            gptService.gptStream(message.getMessage(),jwt,request,response); //执行流操作
        }catch (Exception e){
            System.out.println("输出错误");
            e.printStackTrace();
        }

    }



}
