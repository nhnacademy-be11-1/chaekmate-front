package shop.chaekmate.front.common.logging;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoggingMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor())
                .addPathPatterns("/**") // 모든 경로에 인터셉터를 적용하되,
                .excludePathPatterns(    // 아래의 경로들은 제외합니다.
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/img/**",
                        "/fonts/**",
                        "/favicon.ico",
                        "/error",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/img/**",
                        "/lib/**",
                        "/mail/**"
                );
    }
}
