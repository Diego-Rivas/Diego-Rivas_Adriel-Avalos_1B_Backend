package com.finalBoss.FinalBoss.modules.salones.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
public class SalonRequestDTO {

    @NotBlank(message = "ERR01: El nombre del salón es obligatorio")
    @Size(max = 100, message = "ERR02: El nombre del salón puede tener máximo 100 caracteres")
    private String nombreSalon;

    @NotNull(message = "ERR03: La capacidad es obligatoria")
    @Positive(message = "ERR04: La capacidad debe ser un número entero mayor a cero")
    private Integer capacidad;

    @NotNull(message = "ERR05: El precio de renta es obligatorio")
    @Positive(message = "ERR06: El precio de renta debe ser mayor a cero")
    private Double precioRenta;

    @Size(max = 100, message = "ERR07: La ubicación puede tener máximo 100 caracteres")
    private String ubicacion;
}
