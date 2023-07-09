package com.chatgpt.service;

import com.chatgpt.model.mail.Leave_mail_model;
import com.chatgpt.model.mail.MailListModel;
import com.chatgpt.model.mail.MailMessage_M;
import com.chatgpt.model.pay.PayDataModel;
import com.chatgpt.utio.Mail.Mail;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.*;
//登入日志  今日访问量日志   邮件发送日志
@Service
public class MailServer {
    public static void main(String[] args) {
        MailServer ma=new MailServer();


        List<MailListModel> mail=new ArrayList<>();
        mail.add(new MailListModel("提示", "服务器已被攻击 "));
        mail.add( new MailListModel("攻击时间", new Date().toString()));
        ma.leaveMail(mail);
    }

    //    留言的发送给服务器
    public  Boolean leaveMail(List<MailListModel> mail){
        try {
            String mailTd="";

            for (MailListModel mm : mail) {
                mailTd+= "    <tr>\n" +
                        "        <td><strong>"+mm.name+"</strong></td>\n" +
                        "        <td>"+mm.message+"</td>\n" +
                        "    </tr>\n";
            }

            String htmlContent ="<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <style>\n" +
                    "        table {border-collapse: collapse;}\n" +
                    "        td {padding: 5px; border: 1px solid black; font-size: 20px; }\n" +
                    "        h2 {border-bottom: 1px solid black; padding-bottom: 5px; font-size: 25px;}\n" +
                    "        table {\n" +
                    "            margin: 0 auto;\n" +
                    "            margin-top: 30px;\n" +
                    "        }\n" +
                    "        .title {\n" +
                    "            text-align: center;\n" +
                    "            font-size: 30px;\n" +
                    "            margin-bottom: 20px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1 class=\"title\">GPT日志</h1>\n" +
                    "<table>\n" +
                       mailTd
                    +

                    "</table>\n" +
                    "</body>\n" +
                    "</html>";
            //执行邮件发送操作
            System.out.println("|mail>> 邮件发送 服务器提醒");
            Mail.sendMail(new MailMessage_M("服务器消息提醒","911412667@qq.com",htmlContent,"911412667@qq.com","juikgdksbfjrbfeh"));
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("邮件发送失败");
            return false;
        }
    }


}


