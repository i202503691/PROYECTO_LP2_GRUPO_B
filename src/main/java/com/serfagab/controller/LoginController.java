package com.serfagab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.serfagab.dto.AutenticacionFilter;
import com.serfagab.service.AutenticacionService;
import com.serfagab.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {

    private final AutenticacionService autenticacionService;

    @GetMapping("iniciar-sesion")
    public String iniciarSesion(@ModelAttribute AutenticacionFilter filter, Model model,
            RedirectAttributes flash, HttpSession session) {

        var usuario = autenticacionService.autenticar(filter);

        if (usuario == null) {
            var mensaje = Alert.sweetAlertError("Login y/o clave invalido");
            model.addAttribute("alert", mensaje);
            model.addAttribute("filter", filter);
            return "login";
        }

        session.setAttribute("idUsuario", usuario.getIdUsuario());
        session.setAttribute("fullName", usuario.getFullName());

        var toast = Alert.sweetToast("Bienvenido " + usuario.getFullName(), "success", 5000);
        flash.addFlashAttribute("toast", toast);

        return "redirect:/dashboard";
    }
}
