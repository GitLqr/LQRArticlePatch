package com.lqr.articlepatch.model;

/**
 * Created by Administrator on 2017/3/10.
 */

public class ResultResponse<T> {

    public String has_more;
    public String message;
    public T data;

    public ResultResponse(String has_more, String message, T data) {
        this.has_more = has_more;
        this.message = message;
        this.data = data;
    }
}
