package com.cube.cubetest.dto;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private Integer returnCode;

    private T data;

    public Result() {
    }

    public Result(Integer returnCode, T data) {
        this.returnCode = returnCode;
        this.data = data;
    }

    public Result(Integer returnCode) {
        this.returnCode=returnCode;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
