package com.finalBoss.FinalBoss.modules.salones.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
public class SalonResponseDTO {

    private Long idSalon;
    private String nombreSalon;
    private Integer capacidad;
    private Double precioRenta;
    private String ubicacion;
}
