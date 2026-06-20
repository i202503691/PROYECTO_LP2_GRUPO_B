package com.serfagab.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.serfagab.dto.OrdenCompraFilter;
import com.serfagab.service.OrdenCompraService;
import com.serfagab.service.ProveedorService;
import com.serfagab.service.TipoMaterialService;
import com.serfagab.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("ordencompra")
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;
    private final ProveedorService proveedorService;
    private final TipoMaterialService tipoMaterialService;

    @GetMapping("listado")
    public String listado(@ModelAttribute OrdenCompraFilter filter, Model model) {
        model.addAttribute("lstOrdenes", ordenCompraService.search(filter));
        model.addAttribute("proveedores", proveedorService.getAll());
        model.addAttribute("filter", filter);
        return "ordencompra/listado";
    }

    @GetMapping("nuevo")
    public String nuevo(Model model, HttpSession session) {
        model.addAttribute("proveedores", proveedorService.getAll());
        model.addAttribute("tipos", tipoMaterialService.getAll());
        model.addAttribute("fecha", LocalDate.now());
        return "ordencompra/nuevo";
    }

    @PostMapping("registrar")
    public String registrar(
            @RequestParam Integer proveedor,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
            @RequestParam(required = false) String observaciones,
            @RequestParam("materialId") List<Integer> materialIds,
            @RequestParam("cantidad") List<Double> cantidades,
            @RequestParam("precio") List<Double> precios,
            RedirectAttributes flash) {

        var response = ordenCompraService.create(proveedor, fecha, observaciones, materialIds, cantidades, precios);

        if (!response.success()) {
            flash.addFlashAttribute("alert", Alert.sweetAlertError(response.mensaje()));
            return "redirect:/ordencompra/nuevo";
        }

        var toast = Alert.sweetToast(response.mensaje(), "success", 5000);
        flash.addFlashAttribute("toast", toast);
        return "redirect:/ordencompra/listado";
    }

    @GetMapping("detalle/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        var orden = ordenCompraService.getOne(id);
        var detalles = ordenCompraService.getDetalles(id);
        model.addAttribute("orden", orden);
        model.addAttribute("detalles", detalles);
        return "ordencompra/detalle";
    }

    @PostMapping("cambiar-estado")
    public String cambiarEstado(@RequestParam Integer id, @RequestParam String nuevoEstado, RedirectAttributes flash) {
        var response = ordenCompraService.changeEstado(id, nuevoEstado);
        var toast = Alert.sweetToast(response.mensaje(), "success", 5000);
        flash.addFlashAttribute("toast", toast);
        return "redirect:/ordencompra/listado";
    }
}
