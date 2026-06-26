package com.serfagab.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.serfagab.service.ReporteService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("reporte")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("ordencompra")
    public void ordenCompra(
            @RequestParam Integer id,
            HttpServletResponse response) throws Exception {

        String reportPath = "/reporte/ordenCompra.jrxml";
        
       

        Map<String, Object> params = new HashMap<>();
        params.put("PNumeroOrden", id);
        
        JasperPrint jasperPrint =
                reporteService.getJasperPrint(params, reportPath);

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                String.format("inline; filename=orden-compra-%s.pdf", id));

        OutputStream outputStream = response.getOutputStream();

        JasperExportManager.exportReportToPdfStream(
                jasperPrint,
                outputStream);

        outputStream.flush();
        outputStream.close();
    }
}