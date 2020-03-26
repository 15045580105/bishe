package com.qianlima.reptile.statistics.entity;

/**
 * @description:
 * @author: mahao
 **/
public enum ResultCode {
    //系统
    SUCCESS(0, "成功"),
    PARAM_NOT_BLANK(400, "参数不能为空"),
    DOC_NOT_BLANK(600,"已存在数据"),
    DOC_IS_BLANK(601,"无数据")
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