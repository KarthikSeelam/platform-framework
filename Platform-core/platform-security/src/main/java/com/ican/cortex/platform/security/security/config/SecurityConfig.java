package com.ican.cortex.platform.security.security.config;

import com.ican.cortex.platform.security.jwt.utils.JwtAuthenticationFilter;
import com.ican.cortex.platform.security.jwt.utils.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtTokenProvider jwtTokenProvider;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Value("${spring.app.security.enabled:true}")
    private boolean securityEnabled;


    @Value("#{'${auth.whitelist}'.split(',')}")
    private List<String> whitelist;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF for stateless APIs.
        http.csrf(AbstractHttpConfigurer::disable);

        if (securityEnabled) {

            List<AntPathRequestMatcher> matchers = whitelist.stream()
                    .map(AntPathRequestMatcher::new)
                    .toList();
            // Security is enabled: configure endpoints, session management, filters, and exception handling.
            http.authorizeHttpRequests(auth -> auth
                            .requestMatchers(matchers.toArray(new AntPathRequestMatcher[0])).permitAll()
                            .anyRequest().authenticated())
                    .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                    .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling(exception -> exception
                            .accessDeniedHandler((req, res, ex) -> res.setStatus(403))
                            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                    // Use the new style for HTTP Basic configuration
                    .httpBasic(Customizer.withDefaults());
        } else {
            // Security is disabled: permit all requests.
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                    .httpBasic(Customizer.withDefaults()); // Optionally, you can disable this if not needed.
        }

        return http.build();
    }
}


