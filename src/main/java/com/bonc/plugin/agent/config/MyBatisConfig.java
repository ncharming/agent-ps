package com.bonc.plugin.agent.config;

import org.apache.ibatis.type.TypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/11
 */
@Configuration
public class MyBatisConfig {
    @Bean
    public TypeHandler<List<String>> listToJsonTypeHandler() {
        return new ListToJsonTypeHandler();
    }
}
