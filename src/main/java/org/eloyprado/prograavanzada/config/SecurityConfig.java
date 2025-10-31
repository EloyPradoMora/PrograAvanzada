package org.eloyprado.prograavanzada.config;

import org.eloyprado.prograavanzada.Repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository repository) {
        return username -> repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth->{
                    auth.requestMatchers("/", "/login", "/holaMundo").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/register").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/register").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(form->form
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