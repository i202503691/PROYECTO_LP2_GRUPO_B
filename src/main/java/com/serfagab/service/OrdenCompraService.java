package com.serfagab.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.serfagab.dto.OrdenCompraFilter;
import com.serfagab.dto.ResultadoResponse;
import com.serfagab.model.DetalleOrdenCompra;
import com.serfagab.model.Material;
import com.serfagab.model.OrdenCompra;
import com.serfagab.model.Proveedor;
import com.serfagab.repository.DetalleOrdenCompraRepository;
import com.serfagab.repository.MaterialRepository;
import com.serfagab.repository.OrdenCompraRepository;
import com.serfagab.repository.ProveedorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    private final ProveedorRepository proveedorRepository;
    private final MaterialRepository materialRepository;

    public List<OrdenCompra> getAll() {
        return ordenCompraRepository.findAllByOrderByIdOrdenCompraDesc();
    }

    public List<OrdenCompra> search(OrdenCompraFilter filter) {
        Integer idOC = filter.getIdOrdenCompra();
        Integer idProv = filter.getIdProveedor();
        String estado = filter.getEstado();

        if (estado != null && estado.isBlank()) estado = null;

        return ordenCompraRepository.findAllByFilters(idOC, idProv, estado);
    }

    public OrdenCompra getOne(Integer idOrdenCompra) {
        return ordenCompraRepository.findById(idOrdenCompra).orElseThrow();
    }

    public List<DetalleOrdenCompra> getDetalles(Integer idOrdenCompra) {
        return detalleOrdenCompraRepository.findByIdOrdenCompra(idOrdenCompra);
    }

    @Transactional
    public ResultadoResponse create(
            Integer idProveedor,
            LocalDate fecha,
            String observaciones,
            List<Integer> materialIds,
            List<Double> cantidades,
            List<Double> precios) {
        try {
            Proveedor proveedor = proveedorRepository.findById(idProveedor).orElseThrow();

            OrdenCompra orden = new OrdenCompra();
            orden.setProveedor(proveedor);
            orden.setFecha(fecha);
            orden.setEstado("PENDIENTE");
            orden.setObservaciones(observaciones);
            orden.setTotal(0.0);

            var ordenGuardada = ordenCompraRepository.save(orden);

            double total = 0.0;

            for (int i = 0; i < materialIds.size(); i++) {
                Material material = materialRepository.findById(materialIds.get(i)).orElseThrow();
                double cantidad = cantidades.get(i);
                double precio = precios.get(i);
                double subtotal = cantidad * precio;

                DetalleOrdenCompra detalle = new DetalleOrdenCompra();
                detalle.setOrdenCompra(ordenGuardada);
                detalle.setMaterial(material);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precio);
                detalle.setSubtotal(subtotal);

                detalleOrdenCompraRepository.save(detalle);
                total += subtotal;
            }

            ordenGuardada.setTotal(total);
            ordenCompraRepository.save(ordenGuardada);

            var mensaje = String.format("Orden de Compra con Id %s registrada", ordenGuardada.getIdOrdenCompra());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transaccion");
        }
    }

    @Transactional
    public ResultadoResponse changeEstado(Integer id, String nuevoEstado) {
        var orden = ordenCompraRepository.findById(id).orElseThrow();
        try {
            orden.setEstado(nuevoEstado);
            var mensaje = String.format("Orden de Compra con Id %s cambiada a %s", orden.getIdOrdenCompra(), nuevoEstado);
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transaccion");
        }
    }
}
