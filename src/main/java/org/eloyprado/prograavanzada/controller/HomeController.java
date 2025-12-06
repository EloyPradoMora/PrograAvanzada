package org.eloyprado.prograavanzada.controller;

import org.eloyprado.prograavanzada.Repository.ProductoRepository;
import org.eloyprado.prograavanzada.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import usuario.Producto;
import usuario.Usuario;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.io.IOException;
import java.util.List;
import java.security.Principal;

@Controller
public class HomeController {

    private final ProductoRepository productoRepository;
    private final UsuarioService usuarioService;

    public HomeController(ProductoRepository productoRepository, UsuarioService usuarioService) {
        this.productoRepository = productoRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/inicio")
    public String inicio(Model model) {
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);
        return "inicio";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }

    // Rename Method: "especificar que registra" -> mostrarFormularioRegistro
    @GetMapping("/register")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    // Lógica condicional refactorizada a excepciones y delegada al servicio
    @PostMapping("/register")
    public String processRegistration(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("passwordAgain") String passwordAgain,
            Model model) {
        try {
            usuarioService.registrarUsuario(username, password, passwordAgain);
            model.addAttribute("success", "¡Registro exitoso! Ya puedes iniciar sesión.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", new Usuario(username, ""));
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error inesperado.");
            model.addAttribute("usuario", new Usuario(username, ""));
        }
        return "register";
    }

    @PostMapping("/products/add")
    public String addProduct(
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") int precio,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam("imagen") MultipartFile imagen,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        try {
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre(nombre);
            nuevoProducto.setPrecio(precio);
            if (principal != null) {
                nuevoProducto.setPublisherUsername(principal.getName());
            }
            if (descripcion != null)
                nuevoProducto.setDescripcion(descripcion);
            if (!imagen.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
                Path path = Paths.get("uploads/images/products");
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
                Files.copy(imagen.getInputStream(), path.resolve(fileName));
                nuevoProducto.setImagenUrl("/images/products/" + fileName);
            }
            productoRepository.save(nuevoProducto); // Podrías mover esto a ProductoService también
            redirectAttributes.addFlashAttribute("successMessage", "¡Producto publicado con éxito!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error al publicar el producto: " + e.getMessage());
        }
        return "redirect:/inicio";
    }
}