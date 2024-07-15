package com.bonc.plugin.agent.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.plugin.agent.config.TianQinConfig;
import com.bonc.plugin.agent.entity.RoomReservationEntityVo;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/6
 */
@Configuration
public class RequestUtil {


   private static String appId;
    @Value("${agent.tianqin.appId}")
    public static void setAppId(String appId) {
        RequestUtil.appId = appId;
    }

    private static String appSecret;

    @Value("${agent.tianqin.appSecret}")
    public static void setAppSecret(String appSecret) {
        RequestUtil.appSecret = appSecret;
    }

    private static Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    public static OkHttpClient getInstance() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .build();
        return okHttpClient;
    }


    public static String get(String url) {
        Request request = new Request.Builder().url(url).build();
        logger.info("请求url：{}", url);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // 如果响应码是200，则处理响应内容
                String responseBody = response.body().string();
                return responseBody;
            } else {
                // 如果响应码不是200，则处理错误
                logger.info("请求失败：{}", response);
                return null;
            }
        } catch (IOException e) {
            if (e instanceof java.net.SocketTimeoutException || e instanceof java.net.SocketException) {
                logger.info("请求失败：{}", e.getMessage());
                // 处理超时异常
                return "timeout";
            } else {
                // 处理其他 IO 异常
                return null;
            }

        }
    }

    public static String formData(String url, RoomReservationEntityVo roomReservationEntityVo) {
        // 创建一个FormBody.Builder，用于添加表单字段
        FormBody.Builder formBuilder = new FormBody.Builder();

        // 添加表单字段
        formBuilder.add("meetingname", roomReservationEntityVo.getMeetingname());
        formBuilder.add("startTime", roomReservationEntityVo.getStartTime());
        formBuilder.add("terminalTime", roomReservationEntityVo.getTerminalTime());
        formBuilder.add("name1", roomReservationEntityVo.getName1());
        formBuilder.add("userid", roomReservationEntityVo.getUserid());
        formBuilder.add("sqdeptname", roomReservationEntityVo.getSqdeptname());
        formBuilder.add("telephone", roomReservationEntityVo.getTelephone());

        // 创建请求体
        RequestBody formBody = formBuilder.build();

        // 创建请求
        Request request = new Request.Builder()
                .url(url) // 替换为你的服务器地址
                .post(formBody) // 指定是POST请求
                .build();

        try (Response response = getInstance().newCall(request).execute()) {
            if (response.isSuccessful()) {
                // 如果响应码是200，则处理响应内容
                logger.info("请求成功：{}", response);
                String responseBody = response.body().string();
                return responseBody;
            } else {
                // 如果响应码不是200，则处理错误
                logger.info("请求失败：{}", response);
                return null;
            }
        } catch (IOException e) {
            logger.info("预定会议室报错：{}",e);
            return null;
        }

    }


    public static String post(String url, Object object) {
        String jsonPost = JSON.toJSONString(object);
        logger.info("调用post方法信息，URL：{}，入参：{}", url, jsonPost);
        RequestBody body = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonPost);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = getInstance().newCall(request).execute()) {
            if (response.isSuccessful()) {
                // 如果响应码是200，则处理响应内容
                String responseBody = response.body().string();
                logger.info("responseBody:{}", responseBody);
                return responseBody;
            } else {
                // 如果响应码不是200，则处理错误
                logger.info("请求失败：{}", response);
                return null;
            }
        } catch (IOException e) {
            logger.info("调用webhook报错：{}",e);
            return null;
        }

    }


    public static JSONObject tianQinBody(Map<String, Object> map) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        String random = "000000";
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            Double a = number.nextDouble();
            String b = a.toString();
            random = b.substring(2, 8);
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("随机数生成失败！！！" + nsae);
        }

        //生成token
        String TIMESTAMP = sdf.format(new Date());
        String TRANS_ID = "";
        try {
            TRANS_ID = sdf1.format(sdf.parse(TIMESTAMP)) + random;
        } catch (ParseException e) {
            logger.info("调用天擎报错：{}",e);
        }

        //APP_IDabcTIMESTAMP2016-04-12 15:06:06 100TRANS_ID20160412150606100335423B2732427
        String minToken = "APP_ID" + TianQinConfig.getAppId() + "TIMESTAMP" + TIMESTAMP + "TRANS_ID" + TRANS_ID +  TianQinConfig.getAppSecret();
        //System.out.println(minToken);
        logger.info("生成的token：" + minToken);
        String miToken = DigestUtils.md5Hex(minToken.getBytes()).toLowerCase(Locale.ENGLISH);

        JSONObject json = new JSONObject();

        //请求头
        Map<String, Object> UNI_BSS_HEAD = new HashMap<String, Object>();
        UNI_BSS_HEAD.put("APP_ID",  TianQinConfig.getAppId() ); //接入标识码
        UNI_BSS_HEAD.put("TIMESTAMP", TIMESTAMP);
        UNI_BSS_HEAD.put("TRANS_ID", TRANS_ID);
        UNI_BSS_HEAD.put("TOKEN", miToken);
        UNI_BSS_HEAD.put("RESERVED", new ArrayList<Map>());

        //报文附加信息
        Map<String, Object> UNI_BSS_ATTACHED = new HashMap<String, Object>();
        UNI_BSS_ATTACHED.put("MEDIA_INFO", "");

        json.put("UNI_BSS_HEAD", UNI_BSS_HEAD);
        json.put("UNI_BSS_BODY", map);
        json.put("UNI_BSS_ATTACHED", UNI_BSS_ATTACHED);

//        logger.info("请求信息json为:"+json);

        return json;
    }


    public static String getTianQinResult(JSONObject jsonStr, String url) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
        // 创建Get请求
        long a = System.currentTimeMillis();
        HttpPost httpPost = null;
        httpPost = new HttpPost(url);

        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Accept-Encoding", "");

        StringEntity entity = new StringEntity(jsonStr.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        CloseableHttpResponse response = null;
        Integer status = 0;
        String hawkData = "";
        try {
            // 配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间(单位毫秒)
                    .setConnectTimeout(60000)
                    // 设置请求超时时间(单位毫秒)
                    .setConnectionRequestTimeout(60000)
                    // socket读写超时时间(单位毫秒)
                    .setSocketTimeout(60000)
//                    设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true).build();
            // 将上面的配置信息 运用到这个Get请求里
            httpPost.setConfig(requestConfig);
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpPost);
            status = response.getStatusLine().getStatusCode();
            logger.info("响应状态为:{}" , status);
            long b = System.currentTimeMillis();
//            long resultTime=b-a;
            logger.info("接口调用时长:{}" , Math.subtractExact(b, a));
            if (status == 200) {
                hawkData = EntityUtils.toString(response.getEntity(), "UTF-8");
                logger.info("响应结果---：{}" , hawkData);
                return hawkData;
            } else {
                hawkData = EntityUtils.toString(response.getEntity(), "UTF-8");
                logger.info("响应结果---：{}" , hawkData);
                return hawkData;
            }
        } catch (ClientProtocolException e) {
            logger.info("调用天擎报错：{}",e);
        } catch (org.apache.http.ParseException e) {
            logger.info("调用天擎报错：{}",e);
        } catch (IOException e) {
            logger.info("调用天擎报错：{}",e);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.info("调用天擎报错：{}",e);
            }
        }
        return null;
    }


}
