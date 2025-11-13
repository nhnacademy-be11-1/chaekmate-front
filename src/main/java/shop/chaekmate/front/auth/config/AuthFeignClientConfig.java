package shop.chaekmate.front.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.chaekmate.front.auth.interceptor.FeignCookieInterceptor;

@Configuration
public class AuthFeignClientConfig {

    @Bean
    public FeignCookieInterceptor feignCookieInterceptor() {
        return new FeignCookieInterceptor();
    }
}