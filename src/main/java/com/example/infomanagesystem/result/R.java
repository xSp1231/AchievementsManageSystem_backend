package com.example.infomanagesystem.result;

import lombok.Data;

@Data
public class R {
    private boolean flag;
    private int  code;
    private String message;
    private String token;
    private Object data;

    //自定义构造器

    public R(boolean flag, int code, String message,String token, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.token=token;
        this.data = data;
    }

    public R(boolean flag, int code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public R(boolean flag, int code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public R(boolean flag, int code, Object data) {
        this.flag = flag;
        this.code = code;
        this.data = data;
    }
    public R(boolean flag, String message) {
        this.flag = flag;
        this.message=message;
    }

    public static R success(){
        return new R(true,200,"SUCCESS!");
    }
    public static R error(){
        return new R(false,400,"something wrong has happened!");
    }

}
