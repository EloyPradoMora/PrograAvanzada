package org.eloyprado.prograavanzada;

import org.eloyprado.prograavanzada.Repository.ProductoRepository;
import org.eloyprado.prograavanzada.Repository.UsuarioRepository;
import org.eloyprado.prograavanzada.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import usuario.Producto;
import usuario.Usuario;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
class HomeControllerAdvancedTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    // -------------------------------------------------------------------
    // PRUEBA COMPLEJA: Validar atributos del modelo, orden de llamadas,
    // y comportamiento con lista vacía.
    // -------------------------------------------------------------------
    @Test
    @WithMockUser
    void inicio_ReturnsEmptyListAndStillRendersCorrectly() throws Exception {
        when(productoRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/inicio"))
                .andExpect(status().isOk())
                .andExpect(view().name("inicio"))
                .andExpect(model().attributeExists("productos"))
                .andExpect(model().attribute("productos", List.of()));

        verify(productoRepository, times(1)).findAll();
        verifyNoMoreInteractions(productoRepository);
    }

    // -------------------------------------------------------------------
    // PRUEBA COMPLEJA: Invalidar username vacío en registro
    // -------------------------------------------------------------------
    @Test
    void processRegistration_Fails_WhenUsernameIsEmpty() throws Exception {

        mockMvc.perform(post("/register")
                        .param("username", "")
                        .param("password", "123")
                        .param("passwordAgain", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));

        verify(usuarioRepository, never()).save(any());
    }

    // -------------------------------------------------------------------
    // PRUEBA COMPLEJA: Error interno del repositorio salva bien la excepción
    // -------------------------------------------------------------------
    @Test
    void processRegistration_HandlesRepositoryException() throws Exception {
        when(usuarioRepository.findByUsername("juan"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("123"))
                .thenReturn("encoded123");
        when(usuarioRepository.save(any()))
                .thenThrow(new RuntimeException("DB ERROR"));

        mockMvc.perform(post("/register")
                        .param("username", "juan")
                        .param("password", "123")
                        .param("passwordAgain", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));

        verify(usuarioRepository).findByUsername("juan");
        verify(passwordEncoder).encode("123");
        verify(usuarioRepository).save(any());
    }

    // -------------------------------------------------------------------
    // PRUEBA COMPLEJA: Validar que el controlador NO llame a save() si falló el passwordAgain
    // -------------------------------------------------------------------
    @Test
    void processRegistration_DoesNotCallSave_WhenPasswordsMismatch() throws Exception {

        mockMvc.perform(post("/register")
                        .param("username", "juan")
                        .param("password", "abc")
                        .param("passwordAgain", "xyz"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));

        verify(usuarioRepository, never()).findByUsername(any());
        verify(usuarioRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    // -------------------------------------------------------------------
    // PRUEBA COMPLEJA: Validar que addProduct maneja excepciones
    // -------------------------------------------------------------------
    @Test
    void addProduct_HandlesRepositoryFailureGracefully() throws Exception {
        doThrow(new RuntimeException("Fallo DB"))
                .when(productoRepository).save(any());

        mockMvc.perform(post("/products/add")
                        .param("nombre", "Lapiz")
                        .param("precio", "300"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inicio"))
                .andExpect(flash().attributeExists("errorMessage"));

        verify(productoRepository, times(1)).save(any());
    }

    // -------------------------------------------------------------------
    // PRUEBA COMPLEJA: Validar orden estricto de operaciones en registro
    // -------------------------------------------------------------------
    @Test
    void processRegistration_VerifiesCallOrder_WhenSuccessful() throws Exception {

        when(usuarioRepository.findByUsername("juan"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("123")).thenReturn("encoded123");
        when(usuarioRepository.save(any())).thenReturn(new Usuario());

        mockMvc.perform(post("/register")
                        .param("username", "juan")
                        .param("password", "123")
                        .param("passwordAgain", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("success"));

        InOrder inOrder = inOrder(usuarioRepository, passwordEncoder, usuarioRepository);

        inOrder.verify(usuarioRepository).findByUsername("juan");
        inOrder.verify(passwordEncoder).encode("123");
        inOrder.verify(usuarioRepository).save(any());
    }
}