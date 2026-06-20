package com.serfagab.dto;

import lombok.Data;

@Data
public class OrdenCompraFilter {
    private Integer idOrdenCompra;
    private Integer idProveedor;
    private String estado;
}
