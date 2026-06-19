package com.serfagab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.serfagab.model.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    List<Material> findAllByOrderByIdMaterialDesc();

    @Query("""
            select m
            from Material as m
            where
                (m.tipoMaterial.idTipoMaterial = :idTipoMaterial or :idTipoMaterial is null)
            """)
    List<Material> findAllByFilters(
        @Param("idTipoMaterial") Integer idTipoMaterial
    );
}

