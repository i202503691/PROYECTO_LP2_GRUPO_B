package com.serfagab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.serfagab.model.OrdenCompra;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {

    List<OrdenCompra> findAllByOrderByIdOrdenCompraDesc();

    @Query("""
            select o
            from OrdenCompra as o
            where
                (:idOrdenCompra is null or o.idOrdenCompra = :idOrdenCompra)
                and
                (:idProveedor is null or o.proveedor.idProveedor = :idProveedor)
                and
                (:estado is null or o.estado = :estado)
            """)
    List<OrdenCompra> findAllByFilters(
        @Param("idOrdenCompra") Integer idOrdenCompra,
        @Param("idProveedor") Integer idProveedor,
        @Param("estado") String estado
    );
}
