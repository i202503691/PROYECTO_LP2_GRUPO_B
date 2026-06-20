package com.serfagab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.serfagab.model.DetalleOrdenCompra;

@Repository
public interface DetalleOrdenCompraRepository extends JpaRepository<DetalleOrdenCompra, Integer> {

    @Query("""
            select d
            from DetalleOrdenCompra as d
            where d.ordenCompra.idOrdenCompra = :idOrdenCompra
            """)
    List<DetalleOrdenCompra> findByIdOrdenCompra(@Param("idOrdenCompra") Integer idOrdenCompra);
}
