package com.norbertkoziana.Session.Authentication.sessions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
@Configuration(proxyBeanMethods = false)
@EnableRedisIndexedHttpSession
public class SessionConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.username}")
    private String username;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.port}")
    private Integer port;

    @Value("${spring.redis.ssl:false}")
    private Boolean useSSL;

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setUsername(username);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        redisStandaloneConfiguration.setPort(port);

        if(useSSL){
            LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder =
                    LettuceClientConfiguration.builder();

            lettuceClientConfigurationBuilder.useSsl();

            LettuceClientConfiguration lettuceClientConfiguration = lettuceClientConfigurationBuilder.build();

            return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
        }

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

}
