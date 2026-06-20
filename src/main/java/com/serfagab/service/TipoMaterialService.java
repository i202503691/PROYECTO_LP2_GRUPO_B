package com.serfagab.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.serfagab.dto.ResultadoResponse;
import com.serfagab.model.TipoMaterial;
import com.serfagab.repository.TipoMaterialRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoMaterialService {

    private final TipoMaterialRepository tipoMaterialRepository;

    public List<TipoMaterial> getAll() {
        return tipoMaterialRepository.findAllByOrderByNombre();
    }

    public ResultadoResponse create(TipoMaterial tipoMaterial) {
        try {
            var registro = tipoMaterialRepository.save(tipoMaterial);
            var mensaje = String.format("Tipo de Material con Id %s registrado", registro.getIdTipoMaterial());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transaccion");
        }
    }

    public TipoMaterial getOne(Integer idTipoMaterial) {
        return tipoMaterialRepository.findById(idTipoMaterial).orElseThrow();
    }

    public ResultadoResponse update(TipoMaterial tipoMaterial) {
        try {
            var registro = tipoMaterialRepository.save(tipoMaterial);
            var mensaje = String.format("Tipo de Material con Id %s actualizado", registro.getIdTipoMaterial());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transaccion");
        }
    }

    @Transactional
    public ResultadoResponse changeActive(Integer id) {
        var tipoMaterial = tipoMaterialRepository.findById(id).orElseThrow();
        try {
            tipoMaterial.setActivo(!tipoMaterial.getActivo());
            var estado = tipoMaterial.getActivo() ? "activado" : "desactivado";
            var mensaje = String.format("Tipo de Material con Id %s %s", tipoMaterial.getIdTipoMaterial(), estado);
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transaccion");
        }
    }
}

