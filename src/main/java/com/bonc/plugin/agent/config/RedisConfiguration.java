package com.bonc.plugin.agent.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration
{
    private static final Logger log = LoggerFactory.getLogger(RedisConfiguration.class);


    @Autowired
    private Environment env;


    @Value("${spring.redis.database_zn}")
    private int znDatabase;

//
//    @Value("${spring.redis.database_default}")
//    private int defaultDatabase;


    @Value("${spring.redis.password}")
    private String password;


//    @Value("${spring.redis.timeout:10000}")
    private int timeout=10000;


    @Value("${spring.redis.jedis.pool.max-active:50}")
    private int maxActive;


    @Value("${spring.redis.jedis.pool.max-idle:10}")
    private int maxIdle;


    @Value("${spring.redis.jedis.pool.min-idle:5}")
    private int minIdle;


    @Value("${spring.redis.jedis.pool.max-wait:-1}")
    private int maxWait;



//    @Bean
//    public GenericObjectPoolConfig getPoolConfig() {
//        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
//        poolConfig.setMaxTotal(this.maxActive);
//        poolConfig.setMaxIdle(this.maxIdle);
//        poolConfig.setMinIdle(this.minIdle);
//        poolConfig.setMaxWaitMillis(this.maxWait);
//        return poolConfig;
//    }


    @Bean(name = {"znRedisTemplate"})
    public RedisTemplate getTest1RedisTemplate() {
        if (this.env.containsProperty("spring.redis.host")) {
            log.info("");
            return getRedisTemplate(this.znDatabase);
        }
        log.info("");
        return getClusterRedisTemplate(this.znDatabase);
    }



    private RedisTemplate getRedisTemplate(int database) {
        String host = this.env.getProperty("spring.redis.host");
        String port = this.env.getProperty("spring.redis.port");

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(Integer.parseInt(port));
        configuration.setPassword(RedisPassword.of(this.password));

        LettucePoolingClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofSeconds(this.timeout)).build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, (LettuceClientConfiguration)clientConfiguration);

        factory.setDatabase(database);

        factory.afterPropertiesSet();
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory((RedisConnectionFactory)factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jacksonSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer((RedisSerializer)stringRedisSerializer);

        redisTemplate.setValueSerializer((RedisSerializer)jackson2JsonRedisSerializer);

        redisTemplate.setHashKeySerializer((RedisSerializer)stringRedisSerializer);

        redisTemplate.setHashValueSerializer((RedisSerializer)jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private RedisTemplate getClusterRedisTemplate(int database) {
        String redisNodes = this.env.getProperty("spring.redis.cluster.nodes");

        RedisClusterConfiguration configuration = new RedisClusterConfiguration();
        String[] hosts = redisNodes.split(",");
        Set<RedisNode> nodeList = new HashSet<>();
        for (String hostAndPort : hosts) {
            String[] hostOrPort = hostAndPort.split(":");
            nodeList.add(new RedisNode(hostOrPort[0], Integer.parseInt(hostOrPort[1])));
        }
        configuration.setClusterNodes(nodeList);
        configuration.setPassword(RedisPassword.of(this.password));

        LettucePoolingClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofSeconds(this.timeout)).build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, (LettuceClientConfiguration)clientConfiguration);

        factory.setDatabase(database);

        factory.afterPropertiesSet();
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory((RedisConnectionFactory)factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jacksonSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer((RedisSerializer)stringRedisSerializer);

        redisTemplate.setValueSerializer((RedisSerializer)jackson2JsonRedisSerializer);

        redisTemplate.setHashKeySerializer((RedisSerializer)stringRedisSerializer);

        redisTemplate.setHashValueSerializer((RedisSerializer)jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private Jackson2JsonRedisSerializer jacksonSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }
}
