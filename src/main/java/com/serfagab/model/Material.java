package com.serfagab.model;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material")
    private Integer idMaterial;

    @ManyToOne
    @JoinColumn(name = "id_tipo_material")
    private TipoMaterial tipoMaterial;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "unidad_medida")
    private String unidadMedida;

    @Column(name = "stock_actual")
    private Double stockActual;

    @Column(name = "precio_referencial")
    private Double precioReferencial;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo;

    public String getActivoDescripcion() {
        return activo ? "Activo" : "Inactivo";
    }
}
