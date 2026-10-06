package com.finalBoss.FinalBoss.modules.clientes.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
public class ClienteResponseDTO {

    private Long idCliente;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
}
