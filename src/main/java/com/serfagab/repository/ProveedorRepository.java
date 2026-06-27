package com.serfagab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.serfagab.model.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    boolean existsByRazonSocial(String razonSocial);

    boolean existsByRuc(String ruc);

    boolean existsByRazonSocialAndIdProveedorNot(String razonSocial, Integer idProveedor);

    boolean existsByRucAndIdProveedorNot(String ruc, Integer idProveedor);

    List<Proveedor> findAllByOrderByIdProveedorDesc();

    @Query("""
            select p
            from Proveedor as p
            where
                (p.razonSocial like %:razonSocial% or :razonSocial is null)
                and
                (p.ruc like %:ruc% or :ruc is null)
            """)
    List<Proveedor> findAllByFilters(
        @Param("razonSocial") String razonSocial,
        @Param("ruc") String ruc
    );
}
