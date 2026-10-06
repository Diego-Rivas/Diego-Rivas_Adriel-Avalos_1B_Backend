package com.finalBoss.FinalBoss.modules.salones.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "SALONES")
@Getter @Setter @ToString
public class SalonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SALON")
    private Long idSalon;

    @Column(name = "NOMBRE_SALON", nullable = false, length = 100)
    private String nombreSalon;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    @Column(name = "PRECIO_RENTA", nullable = false)
    private Double precioRenta;

    @Column(name = "UBICACION", length = 100)
    private String ubicacion;
}
