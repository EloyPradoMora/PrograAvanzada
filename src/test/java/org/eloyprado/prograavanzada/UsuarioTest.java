package org.eloyprado.prograavanzada;

import org.junit.jupiter.api.Test;
import usuario.Usuario;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testDefaultRole() {
        Usuario u = new Usuario();
        assertEquals("ROLE_USER", u.getRole());
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
    void constructor_AssignsCorrectDefaults() {
        Usuario u = new Usuario();
        assertEquals("ROLE_USER", u.getRole());
    }
}