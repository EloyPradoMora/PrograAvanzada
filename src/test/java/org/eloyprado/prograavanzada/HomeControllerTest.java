package org.eloyprado.prograavanzada;

import org.eloyprado.prograavanzada.Repository.ProductoRepository;
import org.eloyprado.prograavanzada.Repository.UsuarioRepository;
import org.eloyprado.prograavanzada.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import usuario.Producto;
import usuario.Usuario;

import java.util.List;
import java.util.Optional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void index_ReturnsIndexView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser
    void inicio_ReturnsListaProductos() throws Exception {
        List<Producto> productos = List.of(new Producto("1", "Test", 100));
        Mockito.when(productoRepository.findAll()).thenReturn(productos);

        mockMvc.perform(get("/inicio"))
                .andExpect(status().isOk())
                .andExpect(view().name("inicio"))
                .andExpect(model().attributeExists("productos"));
    }

    @Test
    void login_WithErrorParam_ReturnsLoginView() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginError"));
    }

    @Test
    void register_ReturnsRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    void processRegistration_Fails_WhenPasswordsDontMatch() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "juan")
                        .param("password", "123")
                        .param("passwordAgain", "321")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void processRegistration_Fails_WhenUserAlreadyExists() throws Exception {
        Mockito.when(usuarioRepository.findByUsername("juan"))
                .thenReturn(Optional.of(new Usuario()));

        mockMvc.perform(post("/register")
                        .param("username", "juan")
                        .param("password", "123")
                        .param("passwordAgain", "123")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void processRegistration_Success_WhenEverythingIsValid() throws Exception {
        Mockito.when(usuarioRepository.findByUsername("juan"))
                .thenReturn(Optional.empty());

        Mockito.when(passwordEncoder.encode("123"))
                .thenReturn("encoded123");

        mockMvc.perform(post("/register")
                        .param("username", "juan")
                        .param("password", "123")
                        .param("passwordAgain", "123")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void addProduct_SuccessfulInsert_RedirectsToInicio() throws Exception {
        mockMvc.perform(post("/products/add")
                        .param("nombre", "Lapiz")
                        .param("precio", "500")
                        .param("descripcion", "Azul")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inicio"));
    }
}