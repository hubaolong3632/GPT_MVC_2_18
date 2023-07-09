package com.chatgpt.service;

import com.chatgpt.model.gpt.mvcGpt.GptBalanceModel;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
//@Component
//@Configuration  //扫描bing
//定时执行代码
@EnableScheduling //定义是一个可以自动执行定时任务的类
public class TimeService {



    @Resource
    GptService gptService;


//    初始化密钥设置
    @Bean
    public List<GptBalanceModel> cleck(){
//        List<GptBalanceModel> balanceList = GptStaticModel.getList;
//        List<GptBalanceModel> gptBalanceModels = gptService.from_gpt_keys(); //更新里面所有密钥
//        System.out.println("初始化,更新密钥成功！");

//        return gptBalanceModels;

        return null;
    }

    @Resource
  private   RedisService redisService;


    //每天0点执行一次
    @Scheduled(cron = "0 0 0 * * ?")
    public void sync1() throws InterruptedException {
        System.out.println("执行清理访问次数");
        final int i = redisService.cleaningKey();
        System.out.println("当前有用户"+i);
    }
//
//    //每5秒执行一次
//    @Scheduled(cron = "*/1 * *  * * * ")
//    public void sync2() throws InterruptedException {
//        System.out.println("执行测试1");
//
//    }
}





