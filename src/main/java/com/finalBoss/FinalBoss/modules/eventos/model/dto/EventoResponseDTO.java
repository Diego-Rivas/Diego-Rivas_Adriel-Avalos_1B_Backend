package com.finalBoss.FinalBoss.modules.eventos.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Getter @Setter @ToString
public class EventoResponseDTO {

    private Long idEvento;

    private Long idCliente;
    private String nombreCliente;

    private Long idSalon;
    private String nombreSalon;
    private Double precioRenta;

    private String nombreEvento;
    private LocalDate fechaEvento;
    private Integer cantidadPersonas;
    private Integer cantidadHoras;
    private String estado;
    private Double totalPago;
}
