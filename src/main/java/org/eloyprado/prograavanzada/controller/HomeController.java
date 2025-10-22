package org.eloyprado.prograavanzada.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.eloyprado.prograavanzada.repository.ProductoRepository;
import usuario.Producto;


import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/inicio")
    public String inicio(Model model){
        List<Producto> productos = new ArrayList<Producto>();
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
}