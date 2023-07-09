package com.chatgpt.model.mail;

public class MailMessage_M {
    private  String theme ; //标题
    private  String mail ;//地址
    private  String message;//邮箱
    private  String mail_Key; // 邮箱号
    private  String Password_Key; //  邮箱密码

    public MailMessage_M(String theme, String mail, String message, String mail_Key, String password_Key) {
        this.theme = theme;
        this.mail = mail;
        this.message = message;
        this.mail_Key = mail_Key;
        Password_Key = password_Key;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMail_Key() {
        return mail_Key;
    }

    public void setMail_Key(String mail_Key) {
        this.mail_Key = mail_Key;
    }

    public String getPassword_Key() {
        return Password_Key;
    }

    public void setPassword_Key(String password_Key) {
        Password_Key = password_Key;
    }

    @Override
    public String toString() {

        return "  主题:"+theme+"\n   邮件号:"+mail+"\n   消息:<<("+message+")>>\n   调用的邮箱:"+mail_Key;

    }
}
