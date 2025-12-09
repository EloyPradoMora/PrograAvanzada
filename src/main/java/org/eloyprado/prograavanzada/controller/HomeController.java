package org.eloyprado.prograavanzada.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final ProductoRepository productoRepository;
    private final UsuarioService usuarioService;

    public HomeController(ProductoRepository productoRepository, UsuarioService usuarioService) {
        this.productoRepository = productoRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String index() {
        logger.info("Accessing index page");
        return "index";
    }

    @GetMapping("/inicio")
    public String inicio(Model model) {
        logger.info("Accessing main home page (inicio)");
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);
        return "inicio";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            logger.warn("Login page accessed with error: {}", error);
            model.addAttribute("loginError", true);
        } else {
            logger.info("Accessing login page");
        }
        return "login";
    }

    // Rename Method: "especificar que registra" -> mostrarFormularioRegistro
    @GetMapping("/register")
    public String mostrarFormularioRegistro(Model model) {
        logger.info("Accessing registration form");
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
            @RequestParam("cantidad") int cantidad,
            @RequestParam("estado") String estado,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam("imagen") MultipartFile imagen,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        try {
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre(nombre);
            nuevoProducto.setPrecio(precio);
            nuevoProducto.setCantidad(cantidad);
            nuevoProducto.setEstado(estado);
            nuevoProducto.setEsVisible(true);
            nuevoProducto
                    .setFechaPublicacion(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));

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
            if (principal != null) {
                logger.info("Usuario '{}' agrego nuevo producto: '{}'", principal.getName(), nuevoProducto.getNombre());
            } else {
                logger.info("Usuario 'anónimo' agrego nuevo producto: '{}'", nuevoProducto.getNombre());
            }
            redirectAttributes.addFlashAttribute("successMessage", "¡Producto publicado con éxito!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error al publicar el producto: " + e.getMessage());
        }
        return "redirect:/inicio";
    }

    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable("id") String id, Model model, Principal principal) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            model.addAttribute("producto", producto);
            if (producto.getPublisherUsername() != null) {
                Usuario publisher = usuarioService.obtenerUsuarioPorNombre(producto.getPublisherUsername());
                model.addAttribute("publisher", publisher);
            }
            if (principal != null) {
                model.addAttribute("currentUser", principal.getName());
            }
        }
        return "detalleProducto";
    }
}