package com.serfagab.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.serfagab.dto.ResultadoResponse;
import com.serfagab.model.Material;
import com.serfagab.repository.MaterialRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;

    public List<Material> getAll() {
        return materialRepository.findAllByOrderByIdMaterialDesc();
    }

    public List<Material> search(Integer idTipoMaterial) {
        return materialRepository.findAllByFilters(idTipoMaterial);
    }

    public ResultadoResponse create(Material material) {
        try {
            var registro = materialRepository.save(material);
            var mensaje = String.format("Material con Id %s registrado", registro.getIdMaterial());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transaccion");
        }
    }

    public Material getOne(Integer idMaterial) {
        return materialRepository.findById(idMaterial).orElseThrow();
    }

    public ResultadoResponse update(Material material) {
        try {
            var registro = materialRepository.save(material);
            var mensaje = String.format("Material con Id %s actualizado", registro.getIdMaterial());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transaccion");
        }
    }

    @Transactional
    public ResultadoResponse changeActive(Integer id) {
        var material = materialRepository.findById(id).orElseThrow();
        try {
            material.setActivo(!material.getActivo());
            var estado = material.getActivo() ? "activado" : "desactivado";
            var mensaje = String.format("Material con Id %s %s", material.getIdMaterial(), estado);
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transaccion");
        }
    }
}
