package com.serfagab.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.serfagab.dto.ProveedorFilter;
import com.serfagab.dto.ResultadoResponse;
import com.serfagab.model.Proveedor;
import com.serfagab.repository.ProveedorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public List<Proveedor> getAll() {
        return proveedorRepository.findAllByOrderByIdProveedorDesc();
    }

    public List<Proveedor> search(ProveedorFilter filter) {
        String razonSocial = filter.getRazonSocial();
        String ruc = filter.getRuc();

        if (razonSocial != null && razonSocial.isBlank()) razonSocial = null;
        if (ruc != null && ruc.isBlank()) ruc = null;

        return proveedorRepository.findAllByFilters(razonSocial, ruc);
    }

    public ResultadoResponse create(Proveedor proveedor) {
        try {
            var registro = proveedorRepository.save(proveedor);
            var mensaje = String.format("Proveedor con Id %s registrado", registro.getIdProveedor());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transaccion");
        }
    }

    public Proveedor getOne(Integer idProveedor) {
        return proveedorRepository.findById(idProveedor).orElseThrow();
    }

    public ResultadoResponse update(Proveedor proveedor) {
        try {
            var registro = proveedorRepository.save(proveedor);
            var mensaje = String.format("Proveedor con Id %s actualizado", registro.getIdProveedor());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transaccion");
        }
    }

    @Transactional
    public ResultadoResponse changeActive(Integer id) {
        var proveedor = proveedorRepository.findById(id).orElseThrow();
        try {
            proveedor.setActivo(!proveedor.getActivo());
            var estado = proveedor.getActivo() ? "activado" : "desactivado";
            var mensaje = String.format("Proveedor con Id %s %s", proveedor.getIdProveedor(), estado);
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transaccion");
        }
    }
}
