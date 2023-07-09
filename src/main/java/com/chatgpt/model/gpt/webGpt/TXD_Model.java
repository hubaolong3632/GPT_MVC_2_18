
package com.chatgpt.model.gpt.webGpt;
/***
 * 封装的json方法用来发送消息
 */
public class TXD_Model {

    public String action;
    public Object params;

    public TXD_Model() {
    }

    public TXD_Model(String action, Object params) {
        this.action = action;
        this.params = params;
    }



 public class 合并转发JSON{
        public String message_id;

     public 合并转发JSON(String message_id) {
         this.message_id = message_id;
     }
 }


    public class Body{
        public  long user_id; //qq号
        public  String message; //要发送的消息

        public Body(long user_id, String message) {
            this.user_id = user_id;
            this.message = message;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }


    public class 删除好友{
        public long user_id;

        public 删除好友(long user_id) {
            this.user_id = user_id;
        }
    }

}

