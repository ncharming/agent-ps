package com.bonc.plugin.agent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/17
 */
@Component
@RefreshScope
public class TianQinConfig {

    @Value("${agent.tianqin.appId}")
    private String appId;
    private static String appId_;
    @Value("${agent.tianqin.appSecret}")
    private String appSecret;

    private static String appSecret_;
    @Value("${agent.tianqin.email_url}")
    private String email_url;
    private static String email_url_;

    @Value("${agent.tianqin.reception_url}")
    private String reception_url;
    private static String reception_url_;

    @PostConstruct
    public void setAppId() {
        TianQinConfig.appId_ = this.appId;
    }

    @PostConstruct
    public void setAppSecret() {
        TianQinConfig.appSecret_ = this.appSecret;
    }


    @PostConstruct
    public void setEmail_url() {
        TianQinConfig.email_url_ = this.email_url;
    }

    public static String getAppSecret() {
        return appSecret_;
    }

    public static String getAppId() {
        return appId_;
    }

    public static String getEmail_url() {
        return email_url_;
    }

    public static String getReception_url() {
        return reception_url_;
    }
    @PostConstruct
    public void setReception_url() {
        TianQinConfig.reception_url_ = this.reception_url;
    }

}
