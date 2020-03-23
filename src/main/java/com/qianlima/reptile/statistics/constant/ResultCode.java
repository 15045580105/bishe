package com.qianlima.reptile.statistics.constant;

/**
 * @description:
 * @author: mahao
 **/
public enum ResultCode {
    //系统
    SUCCESS(0, "成功"),
    PARAM_NOT_BLANK(400, "参数不能为空"),
    SERVER_ERROR(10001, "服务器异常")
    ;
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}