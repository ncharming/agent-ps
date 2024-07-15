package com.bonc.plugin.utils;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.util.RedisUtil;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author scott
 * @Date 2019/9/23 14:12
 * @Description: 编程校验token有效性
 */
@Slf4j
public class TokenUtils {

    private static ThreadLocal<LoginUser> loginUserThreadLocal=new ThreadLocal<>();

    /**
     * 从request中获取登录用户信息
     * @param request
     * @return
     */
    public static LoginUser getLoginUserByRequest(HttpServletRequest request){
        String header = request.getHeader("X-LoginUser");
        if(StringUtils.isEmpty(header)){
            return null;
        }
        LoginUser loginUser = JSON.parseObject(header, LoginUser.class);
        return loginUser;
    }

    /**
     * 从threadlocal中获取用户信息
     * @return
     */
    public static LoginUser getLoginUser() {
       return   loginUserThreadLocal.get();
    }

    public static void setLoginUser(LoginUser loginUser){
        loginUserThreadLocal.set(loginUser);
    }

    /**
     * 获取 request 里传递的 token
     *
     * @param request
     * @return
     */
    public static String getTokenByRequest(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (token == null) {
            token = request.getHeader("X-Access-Token");
        }
        return token;
    }


    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e){
            return null;
        }
//        catch (Exception e) {
//            return null;
//        }
    }


}
