package com.rightpair.config;

import com.rightpair.security.AppAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AppAuthenticationEntryPoint appAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(configurer -> configurer.loginPage("/login-form")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login-form?error=true")
                        .permitAll())
                .logout(configurer -> configurer.logoutSuccessUrl("/logout")
                        .deleteCookies("JSESSIONID"))
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(appAuthenticationEntryPoint)
                        .accessDeniedPage("/404"));

        httpSecurity.authorizeHttpRequests(registry ->
                registry.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .anyRequest().authenticated()
        );

        return httpSecurity.getOrBuild();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
