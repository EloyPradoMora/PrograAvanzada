package org.eloyprado.prograavanzada.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityComponentConfig {

    // Esta clase ahora solo define beans de componentes reutilizables.
    // LCOM4 = 1 (Cohesión Funcional)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}