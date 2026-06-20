package com.serfagab.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.serfagab.model.Material;
import com.serfagab.service.MaterialService;
import com.serfagab.service.TipoMaterialService;
import com.serfagab.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("material")
public class MaterialController {

    private final MaterialService materialService;
    private final TipoMaterialService tipoMaterialService;

    @GetMapping("listado")
    public String listado(@RequestParam(required = false) Integer idTipoMaterial, Model model) {
        model.addAttribute("lstMateriales", materialService.search(idTipoMaterial));
        model.addAttribute("tipos", tipoMaterialService.getAll());
        model.addAttribute("idTipoMaterial", idTipoMaterial);
        return "material/listado";
    }

    @GetMapping("nuevo")
    public String nuevo(Model model) {
        model.addAttribute("material", new Material());
        model.addAttribute("tipos", tipoMaterialService.getAll());
        return "material/nuevo";
    }

    @PostMapping("registrar")
    public String registrar(@ModelAttribute Material material, Model model, RedirectAttributes flash) {
        var response = materialService.create(material);

        if (!response.success()) {
            model.addAttribute("material", material);
            model.addAttribute("tipos", tipoMaterialService.getAll());
            model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
            return "material/nuevo";
        }

        var toast = Alert.sweetToast(response.mensaje(), "success", 5000);
        flash.addFlashAttribute("toast", toast);
        return "redirect:/material/listado";
    }

    @GetMapping("edicion/{id}")
    public String edicion(@PathVariable Integer id, Model model) {
        model.addAttribute("material", materialService.getOne(id));
        model.addAttribute("tipos", tipoMaterialService.getAll());
        return "material/edicion";
    }

    @PostMapping("guardar")
    public String guardar(@ModelAttribute Material material, Model model, RedirectAttributes flash) {
        var response = materialService.update(material);

        if (!response.success()) {
            model.addAttribute("material", material);
            model.addAttribute("tipos", tipoMaterialService.getAll());
            model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
            return "material/edicion";
        }

        var toast = Alert.sweetToast(response.mensaje(), "success", 5000);
        flash.addFlashAttribute("toast", toast);
        return "redirect:/material/listado";
    }

    @GetMapping("listado-json")
    @ResponseBody
    public List<Material> listadoJson(@RequestParam(required = false) Integer idTipoMaterial) {
        return materialService.search(idTipoMaterial);
    }

    @PostMapping("cambiar-estado")
    public String cambiarEstado(@RequestParam Integer id, RedirectAttributes flash) {
        var response = materialService.changeActive(id);
        var toast = Alert.sweetToast(response.mensaje(), "success", 5000);
        flash.addFlashAttribute("toast", toast);
        return "redirect:/material/listado";
    }
}
