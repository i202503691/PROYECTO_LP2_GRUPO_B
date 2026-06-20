package com.serfagab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.serfagab.model.TipoMaterial;
import com.serfagab.service.TipoMaterialService;
import com.serfagab.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("tipomaterial")
public class TipoMaterialController {

    private final TipoMaterialService tipoMaterialService;

    @GetMapping("listado")
    public String listado(Model model) {
        model.addAttribute("lstTipos", tipoMaterialService.getAll());
        return "tipomaterial/listado";
    }

    @GetMapping("nuevo")
    public String nuevo(Model model) {
        model.addAttribute("tipoMaterial", new TipoMaterial());
        return "tipomaterial/nuevo";
    }

    @PostMapping("registrar")
    public String registrar(@ModelAttribute TipoMaterial tipoMaterial, Model model, RedirectAttributes flash) {
        var response = tipoMaterialService.create(tipoMaterial);

        if (!response.success()) {
            model.addAttribute("tipoMaterial", tipoMaterial);
            model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
            return "tipomaterial/nuevo";
        }

        var toast = Alert.sweetToast(response.mensaje(), "success", 5000);
        flash.addFlashAttribute("toast", toast);
        return "redirect:/tipomaterial/listado";
    }

    @GetMapping("edicion/{id}")
    public String edicion(@PathVariable Integer id, Model model) {
        model.addAttribute("tipoMaterial", tipoMaterialService.getOne(id));
        return "tipomaterial/edicion";
    }

    @PostMapping("guardar")
    public String guardar(@ModelAttribute TipoMaterial tipoMaterial, Model model, RedirectAttributes flash) {
        var response = tipoMaterialService.update(tipoMaterial);

        if (!response.success()) {
            model.addAttribute("tipoMaterial", tipoMaterial);
            model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
            return "tipomaterial/edicion";
        }

        var toast = Alert.sweetToast(response.mensaje(), "success", 5000);
        flash.addFlashAttribute("toast", toast);
        return "redirect:/tipomaterial/listado";
    }

    @PostMapping("cambiar-estado")
    public String cambiarEstado(@RequestParam Integer id, RedirectAttributes flash) {
        var response = tipoMaterialService.changeActive(id);
        var toast = Alert.sweetToast(response.mensaje(), "success", 5000);
        flash.addFlashAttribute("toast", toast);
        return "redirect:/tipomaterial/listado";
    }
}

