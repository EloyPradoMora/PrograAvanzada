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

        return "admin";
    }

    @GetMapping("/logs/view")
    public org.springframework.http.ResponseEntity<String> viewLogs() {
        try {
            java.nio.file.Path logPath = java.nio.file.Paths.get("logs", "application.log");
            if (java.nio.file.Files.exists(logPath)) {
                String content = java.nio.file.Files.readString(logPath);
                return org.springframework.http.ResponseEntity.ok()
                        .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                        .body(content);
            } else {
                return org.springframework.http.ResponseEntity.ok("El archivo de log no existe aún.");
            }
        } catch (java.io.IOException e) {
            return org.springframework.http.ResponseEntity.internalServerError()
                    .body("Error al leer el log: " + e.getMessage());
        }
    }

    @GetMapping("/logs/download")
    public org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> downloadLogs() {
        try {
            java.nio.file.Path logPath = java.nio.file.Paths.get("logs", "application.log");
            if (java.nio.file.Files.exists(logPath)) {
                org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(
                        logPath.toUri());
                return org.springframework.http.ResponseEntity.ok()
                        .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"application.log\"")
                        .body(resource);
            } else {
                return org.springframework.http.ResponseEntity.notFound().build();
            }
        } catch (java.net.MalformedURLException e) {
            return org.springframework.http.ResponseEntity.internalServerError().build();
        }
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