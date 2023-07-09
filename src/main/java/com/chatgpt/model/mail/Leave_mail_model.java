package com.chatgpt.model.mail;

import com.chatgpt.model.login.LoginYesModel;

/**邮件模板*/
public class Leave_mail_model {
    public Integer id;
    public String name;
    public String phone;
    public String mail;
    public String message;
    public String date; //时间
    public String ip; //ip

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Leave_mail_model(String name, String phone, String mail, String message, String date) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.message = message;
        this.date = date;
    }

    public Leave_mail_model(String name, String phone, String mail, String message) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.message = message;
    }

    public Leave_mail_model() {
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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


}
