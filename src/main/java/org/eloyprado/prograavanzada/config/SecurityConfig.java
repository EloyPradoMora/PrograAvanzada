package org.eloyprado.prograavanzada.config;

import org.eloyprado.prograavanzada.Repository.UsuarioRepository; // Importar
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService; // Importar
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importar
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importar
import org.springframework.security.crypto.password.PasswordEncoder; // Importar
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Bean para codificar las contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Bean para cargar usuarios desde MongoDB (Lectura de usuarios)
    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository repository) {
        // Usa el repositorio para encontrar el usuario por nombre.
        return username -> repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth->{
            auth.requestMatchers("/","/login", "/holaMundo").permitAll(); // Se agregó /holaMundo como permitida
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
                .build();
    }
}