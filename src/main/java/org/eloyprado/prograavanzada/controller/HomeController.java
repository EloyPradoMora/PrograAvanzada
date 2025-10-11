package org.eloyprado.prograavanzada.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import usuario.Producto;


import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/inicio")
    public String inicio(Model model){
        List<Producto> productos = new ArrayList<Producto>();
        productos.add(new Producto("1", "Acer Nitro 5", 600000));
        productos.add(new Producto("2", "Polera niño", 1000));
        productos.add(new Producto("3", "De sodorante", 4000));
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