package com.chatgpt.utio.Mail;


import com.chatgpt.model.mail.Leave_mail_model;
import com.chatgpt.model.mail.MailMessage_M;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Mail {
    public static String myEmailAccount = "911412667@qq.com"; //发送邮件的账号        模拟数据主要得看发送的
    public static String myEmailPassword = "juikgdksbfjrbfeh"; //发送邮件的密码          模拟数据主要得看发送的
//    public static String myEmailSMTPHost = "smtp.163.com"; //邮件服务器的ip地址
    public static String myEmailSMTPHost = "smtp.qq.com"; //邮件服务器的ip地址
    public static int port=25;//邮件服务器端口

    public static Properties props;//邮件服务器的配置
    static {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        props= new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.put("mail.smtp.port", port);
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
    }




    //邮件的发送
    public static void sendMail(MailMessage_M msg) throws Exception {
        myEmailAccount=msg.getMail_Key();//账号
        myEmailPassword=msg.getPassword_Key(); //密码
//        System.out.println("   端口:"+port+"     ip:"+myEmailSMTPHost);

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        session.setDebug(false);
        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, myEmailAccount, msg);
        message.addHeader("X-Custom-Header", "Custom Value");
        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        transport.connect(myEmailAccount, myEmailPassword);
        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());
        System.out.println("邮件发送成功!");
    }



    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session     和服务器交互的会话
     * @param sendMail    发件人邮箱
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail,MailMessage_M msg) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, msg.getTheme(), "UTF-8"));
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(msg.getMail(), "XX用户", "UTF-8"));
        // 4. Subject: 邮件主题
        message.setSubject(msg.getTheme(), "UTF-8");
        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(msg.getMessage(), "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());
        // 7. 保存设置
        message.saveChanges();
        return message;
    }
}