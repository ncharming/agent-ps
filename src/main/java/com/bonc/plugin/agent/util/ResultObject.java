package com.bonc.plugin.agent.util;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/6
 */
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.io.Serializable;

/**
 *   接口返回数据格式
 * @author scott
 * @email jeecgos@163.com
 * @date  2019年1月19日
 */
@Data
public class ResultObject<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */

    private boolean success = true;

    /**
     * 返回处理消息
     */

    private String message = "";

    /**
     * 返回代码
     */

    private Integer code = 0;

    /**
     * 返回数据对象 data
     */

    private T result;

    /**
     * 时间戳
     */

    private long timestamp = System.currentTimeMillis();

    public ResultObject() {
    }

    /**
     * 兼容VUE3版token失效不跳转登录页面
     * @param code
     * @param message
     */
    public ResultObject(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultObject<T> success(String message) {
        this.message = message;
        this.code = 200;
        this.success = true;
        return this;
    }

    public static<T> ResultObject<T> ok() {
        ResultObject<T> r = new ResultObject<T>();
        r.setSuccess(true);
        r.setCode(200);
        return r;
    }

    public static<T> ResultObject<T> ok(String msg) {
        ResultObject<T> r = new ResultObject<T>();
        r.setSuccess(true);
        r.setCode(200);
        //ResultObject OK(String msg)方法会造成兼容性问题 issues/I4IP3D
        r.setResult((T) msg);
        r.setMessage(msg);
        return r;
    }

    public static<T> ResultObject<T> ok(T data) {
        ResultObject<T> r = new ResultObject<T>();
        r.setSuccess(true);
        r.setCode(200);
        r.setResult(data);
        return r;
    }

    public static<T> ResultObject<T> OK() {
        ResultObject<T> r = new ResultObject<T>();
        r.setSuccess(true);
        r.setCode(200);
        return r;
    }

    /**
     * 此方法是为了兼容升级所创建
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static<T> ResultObject<T> OK(String msg) {
        ResultObject<T> r = new ResultObject<T>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage(msg);
        //ResultObject OK(String msg)方法会造成兼容性问题 issues/I4IP3D
        r.setResult((T) msg);
        return r;
    }

    public static<T> ResultObject<T> OK(T data) {
        ResultObject<T> r = new ResultObject<T>();
        r.setSuccess(true);
        r.setCode(200);
        r.setResult(data);
        return r;
    }

    public static<T> ResultObject<T> OK(String msg, T data) {
        ResultObject<T> r = new ResultObject<T>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public static<T> ResultObject<T> error(String msg, T data) {
        ResultObject<T> r = new ResultObject<T>();
        r.setSuccess(false);
        r.setCode(500);
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public static<T> ResultObject<T> error(String msg) {
        return error(500, msg);
    }

    public static<T> ResultObject<T> error(int code, String msg) {
        ResultObject<T> r = new ResultObject<T>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public ResultObject<T> error500(String message) {
        this.message = message;
        this.code = 500;
        this.success = false;
        return this;
    }

    /**
     * 无权限访问返回结果
     */
    public static<T> ResultObject<T> noauth(String msg) {
        return error(401, msg);
    }

    @JsonIgnore
    private String onlTable;

}
