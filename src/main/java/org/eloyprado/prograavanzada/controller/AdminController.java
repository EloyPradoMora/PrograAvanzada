package org.eloyprado.prograavanzada.controller;

import org.eloyprado.prograavanzada.Repository.ProductoRepository;
import org.eloyprado.prograavanzada.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import usuario.Producto;
import usuario.Usuario;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductoRepository productoRepository;
    private final UsuarioService usuarioService;

    public AdminController(ProductoRepository productoRepository, UsuarioService usuarioService) {
        this.productoRepository = productoRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String adminHome(Model model) {
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);
        List<Usuario> allUsers = usuarioService.obtenerTodos();
        List<Usuario> nonAdmins = allUsers.stream()
                .filter(u -> !"ROLE_ADMIN".equals(u.getRole()))
                .toList();

        model.addAttribute("allUsers", allUsers);
        model.addAttribute("nonAdmins", nonAdmins);

        model.addAttribute("userLogs", List.of("Log 1: User Login", "Log 2: User Error", "Log 3: User Update"));
        model.addAttribute("productLogs",
                List.of("Log 1: Product Added (Publisher)", "Log 2: Product Sold (Publisher)"));
        model.addAttribute("accessLogs", List.of("Log 1: Access /admin", "Log 2: Access /inicio"));
        return "admin";
    }

    @PostMapping("/promote-user")
    public String promoteUser(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.cambiarRol(username, "ROLE_ADMIN");
            redirectAttributes.addFlashAttribute("successMessage", "¡Usuario " + username + " promovido a Admin!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al promover usuario: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/demote-user")
    public String demoteUser(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.cambiarRol(username, "ROLE_USER");
            redirectAttributes.addFlashAttribute("successMessage", "¡Admin " + username + " degradado a Usuario!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al degradar admin: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(username);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario " + username + " eliminado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}