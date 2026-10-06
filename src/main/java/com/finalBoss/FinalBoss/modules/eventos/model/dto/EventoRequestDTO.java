package com.finalBoss.FinalBoss.modules.eventos.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Getter @Setter @ToString
public class EventoRequestDTO {

    @NotNull(message = "ERR01: Debe seleccionar un cliente")
    private Long idCliente;

    @NotNull(message = "ERR02: Debe seleccionar un salón")
    private Long idSalon;

    @NotBlank(message = "ERR03: El nombre del evento es obligatorio")
    @Size(max = 100, message = "ERR04: El nombre del evento puede tener máximo 100 caracteres")
    private String nombreEvento;

    @NotNull(message = "ERR05: La fecha del evento es obligatoria")
    private LocalDate fechaEvento;

    @NotNull(message = "ERR06: La cantidad de personas es obligatoria")
    @Positive(message = "ERR07: La cantidad de personas debe ser mayor a cero")
    private Integer cantidadPersonas;

    @NotNull(message = "ERR08: La cantidad de horas es obligatoria")
    @Positive(message = "ERR09: La cantidad de horas debe ser mayor a cero")
    private Integer cantidadHoras;
}
