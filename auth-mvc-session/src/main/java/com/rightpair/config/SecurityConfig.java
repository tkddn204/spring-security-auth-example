package com.rightpair.config;

import com.rightpair.security.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AppUserDetailsService appUserDetailsService;
    private final RedisIndexedSessionRepository redisIndexedSessionRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(configurer -> configurer.loginPage("/users/login")
                        .failureUrl("/users/login?error")
                        .permitAll())
                .userDetailsService(appUserDetailsService)
                .logout(configurer -> configurer.logoutSuccessUrl("/users/login?logout")
                        .deleteCookies("JSESSIONID"))
                .sessionManagement(configurer -> configurer.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId)
                        .maximumSessions(100)
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(springSessionBackedSessionRegistry()));

        httpSecurity.authorizeHttpRequests(registry ->
                registry.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers("/", "/error").permitAll()
                        .requestMatchers("/users/login", "/users/signup").permitAll()
                        .anyRequest().authenticated()
        );

        return httpSecurity.getOrBuild();
    }

    @Bean
    public SessionRegistry springSessionBackedSessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(redisIndexedSessionRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
