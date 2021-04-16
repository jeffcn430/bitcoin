package com.sf.bitcoin.controller;

import lombok.Data;

@Data
public class ResultData {
    public static final String SUCCESS = "成功";
    public static final String FAIL = "失败";

    /**
     * 状态
     */
    public Integer code;
    /**
     * 状态信息
     */
    public String msg;
    /**
     * 返回数据
     */
    public Object data;
    /**
     * 返回数据数量
     */
    private int count;

    public ResultData() {
        this.code = 0;
        this.msg = SUCCESS;
    }

    public ResultData(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultData(Object data) {
        this.code = 0;
        this.msg = SUCCESS;
        this.data = data;
        this.count = 0;
    }

    public ResultData(Object data, int count) {
        this.code = 0;
        this.msg = SUCCESS;
        this.data = data;
        this.count = count;
    }
}
