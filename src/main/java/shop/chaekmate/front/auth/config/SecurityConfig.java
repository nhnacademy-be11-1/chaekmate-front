package shop.chaekmate.front.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shop.chaekmate.front.auth.filter.JwtAuthenticationFilter;
import shop.chaekmate.front.auth.handler.CustomLogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomLogoutHandler customLogoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 관리자 로그인 페이지는 누구나 접근 가능
                        .requestMatchers("/admin/login").permitAll()
                        // 관리자 페이지는 ADMIN 권한 필요
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 비회원도 접근 되는 곳들
                        .requestMatchers("/", "/login", "/signup").permitAll()
                        .requestMatchers("/books/**").permitAll() // 도서 상세, 도서 목록
                        .requestMatchers("/categories/**").permitAll() // 카테고리별 도서 목록
                        .requestMatchers("/payments/**").permitAll() // 결제 페이지
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**",
                                "/favicon.ico")
                        .permitAll()
                        // 회원 전용
                        .requestMatchers("/mypage/**", "/logout").authenticated()
                        .requestMatchers("/order").authenticated() // 주문 페이지
                        // 나머지는 인증 필요
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (request, response, authException) -> response.sendRedirect("/login")))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessUrl("/"))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}