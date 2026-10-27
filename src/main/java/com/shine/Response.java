package com.shine;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ━━━━━━━━━佛祖保佑━━━━━━━━━━━
 *                  ,;,,;
 *                ,;;' (    社
 *      ___      ,;;' ' \   会
 *    /'  '\'~~'~' \ /'\.)  主
 * ,;(      )     /  |.     义
 *,;' \    /-.,,(   ) \     码
 *     ) /       ) / )|     农
 *     ||        ||  \\
 *    (_\       (_\  (_\
 * ━━━━━━━━━永无BUG━━━━━━━━━━━━
 *
 * @author Shine
 * @since 2025/8/11 10:07
 */

@Data
public class Response<T> implements Serializable {

    public static int ERROR = 500;
    public static int SUCCESS = 200;
    /**
     * 同一个手机号绑定了多个公司
     */
    public static int MULTIPLE_ACCOUNTS = 201;
    /**
     * 登录无效
     */
    public static int LOGIN = -10001;

    public static int LOGIN_403 = 403;

    int code;

    String msg;

    T data;

    String time;

    Float balance;


    public static <T> Response<T> create(int code, String msg, T data) {
        return new Response<>(code, msg, data);
    }

    public static <T> Response<T> error(String msg) {
        return new Response<>(ERROR, msg);
    }

    public static <T> Response<T> error(String msg, T data) {
        return new Response<>(ERROR, msg, data);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(SUCCESS, null, data);
    }

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(SUCCESS, message, data);
    }

    public static <T> Response<T> create(int code, String msg) {
        return new Response<>(code, msg);
    }

    public static <T> Response<T> create(int code) {
        return new Response<>(code);
    }

    private Response(int code, String msg, T data) {
        this.code = code;
        this.data = data;

        if (msg == null || msg.isEmpty()) {
            if (code == SUCCESS) {
                this.msg = "SUCCESS";
            } else {
                this.msg = "ERROR";
            }
        } else {
            this.msg = msg;
        }
        setTime();
    }

    private Response(int code, String msg) {
        this.code = code;
        if (msg == null || msg.isEmpty()) {
            if (code == SUCCESS) {
                this.msg = "SUCCESS";
            } else {
                this.msg = "ERROR";
            }
        } else {
            this.msg = msg;
        }
        setTime();
    }

    private Response(int code) {
        this.code = code;
        if (code == SUCCESS) {
            this.msg = "SUCCESS";
        } else {
            this.msg = "ERROR";
        }
        setTime();
    }

    private void setTime() {
        this.time = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
}
