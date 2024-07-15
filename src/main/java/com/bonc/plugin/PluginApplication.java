package com.bonc.plugin;

import org.jeecg.common.modules.redis.config.RedisConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = QuartzAutoConfiguration.class)
@ComponentScan(basePackages = {"org.jeecg", "com.bonc"})
@EnableAsync
@EnableFeignClients(basePackages = {"org.jeecg", "com.bonc"})
public class PluginApplication implements CommandLineRunner {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(PluginApplication.class, args);
    }


    /**
     * 启动的时候，触发下 Gateway网关刷新
     * <p>
     * 解决： 先启动gateway后启动服务，Swagger接口文档访问不通的问题
     *
     * @param args args
     */
    @Override
    public void run(String... args) {
//        BaseMap params = new BaseMap();
//        params.put(GlobalConstants.HANDLER_NAME, GlobalConstants.LODER_ROUDER_HANDLER);
//        //刷新网关
//        redisTemplate.convertAndSend(GlobalConstants.REDIS_TOPIC_NAME, params);
    }
}
