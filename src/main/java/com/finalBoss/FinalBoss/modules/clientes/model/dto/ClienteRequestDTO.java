package com.finalBoss.FinalBoss.modules.clientes.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
public class ClienteRequestDTO {

    @NotBlank(message = "ERR01: El nombre es obligatorio")
    @Size(max = 100, message = "ERR02: El nombre puede tener máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "ERR03: El apellido es obligatorio")
    @Size(max = 100, message = "ERR04: El apellido puede tener máximo 100 caracteres")
    private String apellido;

    @NotBlank(message = "ERR05: El teléfono es obligatorio")
    @Size(max = 15, message = "ERR06: El teléfono puede tener máximo 15 caracteres")
    private String telefono;

    @NotBlank(message = "ERR07: El email es obligatorio")
    @Email(message = "ERR08: El email no tiene un formato válido")
    @Size(max = 100, message = "ERR09: El email puede tener máximo 100 caracteres")
    private String email;


    @Size(max = 200, message = "ERR10: La dirección puede tener máximo 200 caracteres")
    private String direccion;
}
