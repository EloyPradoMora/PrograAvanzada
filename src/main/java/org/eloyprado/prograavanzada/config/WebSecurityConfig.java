package org.eloyprado.prograavanzada.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig { // Anteriormente 'SecurityConfig'

    // Esta clase ahora solo tiene la responsabilidad de configurar las reglas HTTP.
    // LCOM4 = 1 (Cohesión Funcional)
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/", "/login", "/holaMundo").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/register").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/register").permitAll();
            auth.anyRequest().authenticated();
        })
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/inicio", true)
                        .permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/inicio", true)
                        .permitAll())
                .csrf(csrf -> csrf.disable())
                .build();
    }
}