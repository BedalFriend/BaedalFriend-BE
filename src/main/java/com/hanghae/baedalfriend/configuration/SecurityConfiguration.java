package com.hanghae.baedalfriend.configuration;

import com.hanghae.baedalfriend.jwt.AccessDeniedHandlerException;
import com.hanghae.baedalfriend.jwt.AuthenticationEntryPointException;
import com.hanghae.baedalfriend.jwt.TokenProvider;
import com.hanghae.baedalfriend.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

    @Value("${jwt.secret}")
    String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationEntryPointException authenticationEntryPointException;
    private final AccessDeniedHandlerException accessDeniedHandlerException;
    private final CorsConfiguration corsConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();

        http.csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointException)
                .accessDeniedHandler(accessDeniedHandlerException)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)


                .and()
                .authorizeRequests()
//                .antMatchers("/api/member/**").permitAll()
                .antMatchers("/api/post/**").permitAll()
                .antMatchers("/v1/members/**").permitAll()
                .antMatchers("/v1/kakao/**").permitAll()
                .antMatchers("/v1/auth/**").permitAll()
                .antMatchers("/v1/posts/**").permitAll()
                .antMatchers("/api/comment/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/events}").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/events/{eventId}").permitAll()
                .antMatchers("/ws/chat").permitAll()
                .antMatchers("/sub/**").permitAll()
                .antMatchers("/pub/**").permitAll()
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/stomp/**").permitAll()
                .antMatchers("/websocket/**").permitAll()
                .antMatchers("/chat/**").permitAll()
                .antMatchers( "/v2/api-docs",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .addFilter(corsConfiguration.corsFilter())
                .apply(new JwtSecurityConfiguration(SECRET_KEY, tokenProvider, userDetailsService));

        return http.build();
    }
}
