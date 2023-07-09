package com.chatgpt.utio.UtioCode;

import java.io.Serializable;

/**
 * Created by zhumaer on 17/5/24.
 */
public class Result implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;

    private Integer code;

    private String msg;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private Object data;

    public Result() {}

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    public static Result success(Object data) { //成功的状态码
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }




    public static Result loginError(Object data) { //登入失败
        Result result = new Result();
        result.setResultCode(ResultCode.USER_LOGIN_ERROR);
        result.setData(data);
        return result;
    }

    public static Result makeOnesOwnChoice(Object data,ResultCode res) { //自定义失败语句
        Result result = new Result();
        result.setResultCode(res);
        result.setData(data);
        return result;
    }


    public static Result Not___SYSTEM_INNER_ERROR(Object date) { //登入失败
        Result result = new Result();
        result.setResultCode(ResultCode.SYSTEM_INNER_ERROR);
        result.setData(date);
        return result;
    }

    public static Result failure(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        return result;
    }

    public static Result failure(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    public void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }
}

