package com.chatgpt.utio.UtioClass;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtio {
 private  static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 时间格式式
    public static String dateDay(){ //获取当前在线时间
        String customDateStr = dateFormat.format(new Date());
        return customDateStr; //返回时间

    }

//    吧这个格式在转换成date类型
//    Date date = format.parse(dateString);
}
