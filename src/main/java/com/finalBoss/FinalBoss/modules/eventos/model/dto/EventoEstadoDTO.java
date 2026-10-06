package com.finalBoss.FinalBoss.modules.eventos.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
public class EventoEstadoDTO {

    @NotBlank(message = "ERR10: El estado es obligatorio")
    @Pattern(regexp = "PENDIENTE|CONFIRMADO|CANCELADO|FINALIZADO",
             message = "ERR11: Estado no válido. Use: PENDIENTE, CONFIRMADO, CANCELADO o FINALIZADO")
    private String estado;
}
