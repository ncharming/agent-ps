package com.bonc.plugin.agent.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/12
 */
@Slf4j
@Component
public class RedisCache {

    public static String firstKey="sys:cache:encrypt:session::";

    private static RedisTemplate redisTemplate;



    private static RedisTemplate znRedisTemplate;
    @Autowired
    public void setZnRedisTemplate(@Qualifier("znRedisTemplate") RedisTemplate znRedisTemplate) {
        RedisCache.znRedisTemplate = znRedisTemplate;
    }


    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisCache.redisTemplate = redisTemplate;
    }

    //获取用户对应的信息
    public static JSONObject getData(String username) {

        //其余数据从redis拿
        String redisKey = "sys:cache:encrypt:user::" + username + "::plugin";
        String redisKey_bak = "sys:cache:encrypt:user::" + username + "::cloud";
        log.info("redisKey:{}", redisKey);
        Object objects = redisTemplate.opsForValue().get(redisKey);

        log.info("redis_plugin 获取的参数：{}", objects);
        if (objects == null) {
            log.info("redisKey:{}", redisKey_bak);
            objects = redisTemplate.opsForValue().get(redisKey_bak);
            log.info("redis_cloud 获取的参数：{}", objects);
            if (objects == null) {
                log.info("两个key值都获取为空：");
                return null;
            }
        }

        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(objects));
        JSONObject otherInfo = jsonObject.getJSONObject("otherInfo");

        if (otherInfo == null) {  //从触点的数据被内部覆盖
            jsonObject.put("orgName", "测试部门");
            jsonObject.put("user", jsonObject.getString("username"));
            jsonObject.put("name", jsonObject.getString("realname"));
            return jsonObject;
        }

        //部门名称
        // otherInfo.put("orgName",otherInfo.getString("orgName").replaceAll("上海市分公司","").replaceAll("上海市",""));

        log.info("redis 获取的参数，json格式化后：{}", jsonObject);
        return otherInfo;

    }


    public static boolean getFirst(String sessionId){
        String key = firstKey+sessionId;
        if(redisTemplate.hasKey(key)){
            return true;
        }
        return false;
    }

    public static void setFirst(String sessionId){
        String key = firstKey+sessionId;
        redisTemplate.opsForValue().set(key,true);
        redisTemplate.expire(key,48, TimeUnit.HOURS);
    }

    public static void delFirst(String sessionId){
        String key = firstKey+sessionId;
        redisTemplate.delete(key);
    }

    public static void delCaowei(String sessionId){
        String key = "session_manager:"+sessionId;
        znRedisTemplate.opsForHash().delete(key,"control_tab","control_tab_timestamp");
    }


}
