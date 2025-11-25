package org.eloyprado.prograavanzada;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import usuario.Usuario;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testDefaultRole() {
        Usuario u = new Usuario();
        assertEquals("ROLE_USER", u.getRole());
    }

    @Test
    void testAuthorities() {
        Usuario u = new Usuario("juan", "123", "ROLE_ADMIN");
        assertTrue(
                u.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    @Test
    void testGettersSetters() {
        Usuario u = new Usuario();

        u.setId("10");
        u.setUsername("elo");
        u.setPassword("pass");
        u.setRole("ROLE_ADMIN");

        assertEquals("10", u.getId());
        assertEquals("elo", u.getUsername());
        assertEquals("pass", u.getPassword());
        assertEquals("ROLE_ADMIN", u.getRole());
    }

    @Test
    void testUserDetailsDefaults() {
        Usuario u = new Usuario();
        assertTrue(u.isAccountNonExpired());
        assertTrue(u.isAccountNonLocked());
        assertTrue(u.isCredentialsNonExpired());
        assertTrue(u.isEnabled());
    }
}