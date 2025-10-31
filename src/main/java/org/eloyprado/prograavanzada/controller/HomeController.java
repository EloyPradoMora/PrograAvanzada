package org.eloyprado.prograavanzada.controller;

import org.eloyprado.prograavanzada.Repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Autowired; 
import org.eloyprado.prograavanzada.Repository.ProductoRepository; 
import usuario.Producto;
import usuario.Usuario;


import java.util.List; // Solo necesitamos List

@Controller
public class HomeController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/inicio")
    public String inicio(Model model){
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);
        return "inicio";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false)String error, Model model){
        if (error != null) {
            model.addAttribute("loginError", true);
            System.out.println("login error");
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("passwordAgain") String passwordAgain,
            Model model
    ) {
        //Ver que las contraseñas coincidan
        if (!password.equals(passwordAgain)) {
            model.addAttribute("error", "Las contraseñas no coinciden. Inténtalo de nuevo.");
            model.addAttribute("usuario", new Usuario(username, ""));
            return "register";
        }
        //Ver que el usuario no este registrado en MongoDB
        if (usuarioRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El nombre de usuario ya está en uso.");
            model.addAttribute("usuario", new Usuario(username, ""));
            return "register";
        }
        //Crear, codificar y guardar el usuario
        try {
            Usuario newUser = new Usuario();
            newUser.setUsername(username);
            // Codificar la contraseña antes de guardar, esto para la seguridad
            newUser.setPassword(passwordEncoder.encode(password));
            usuarioRepository.save(newUser);
            model.addAttribute("success", "¡Registro exitoso! Ya puedes iniciar sesión.");
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al registrar. Inténtalo más tarde.");
            model.addAttribute("usuario", new Usuario(username, ""));
            return "register";
        }
    }
}