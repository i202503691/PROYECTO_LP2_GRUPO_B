package com.serfagab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serfagab.model.TipoMaterial;

@Repository
public interface TipoMaterialRepository extends JpaRepository<TipoMaterial, Integer> {

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdTipoMaterialNot(String nombre, Integer idTipoMaterial);

    List<TipoMaterial> findAllByOrderByNombre();
}
