package com.finalBoss.FinalBoss.modules.clientes.controller;

import com.finalBoss.FinalBoss.exceptions.BusinessRuleException;
import com.finalBoss.FinalBoss.modules.clientes.model.dto.ClienteRequestDTO;
import com.finalBoss.FinalBoss.modules.clientes.model.dto.ClienteResponseDTO;
import com.finalBoss.FinalBoss.modules.clientes.service.ClienteService;
import com.finalBoss.FinalBoss.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteResponseDTO>>> obtenerDatos() {
        try {
            List<ClienteResponseDTO> lista = service.obtenerTodos();
            ApiResponse<List<ClienteResponseDTO>> respuestaExitosa = new ApiResponse<>(true, "Proceso completado", lista);
            return ResponseEntity.ok(respuestaExitosa);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<List<ClienteResponseDTO>> respuestaError = new ApiResponse<>(false, "No se pudieron obtener los clientes", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> obtenerDatosId(@PathVariable Long id) {
        try {
            ClienteResponseDTO dto = service.obtenerPorId(id);
            if (dto != null) {
                ApiResponse<ClienteResponseDTO> respuestaExitosa = new ApiResponse<>(true, "Cliente encontrado", dto);
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<ClienteResponseDTO> noEncontrado = new ApiResponse<>(false, "El cliente con ID: " + id + " no se encontró", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<ClienteResponseDTO> respuestaError = new ApiResponse<>(false, "No se pudo obtener el cliente", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }


    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> nuevoCliente(@Valid @RequestBody ClienteRequestDTO json) {
        try {
            ClienteResponseDTO dto = service.nuevoCliente(json);
            ApiResponse<ClienteResponseDTO> respuesta = new ApiResponse<>(true, "Cliente registrado", dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (BusinessRuleException e) {
            ApiResponse<ClienteResponseDTO> respuestaError = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaError);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<ClienteResponseDTO> respuestaError = new ApiResponse<>(false, "No se pudo registrar el cliente", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO json) {
        try {
            ClienteResponseDTO dto = service.actualizarCliente(json, id);
            if (dto != null) {
                ApiResponse<ClienteResponseDTO> respuestaExitosa = new ApiResponse<>(true, "Cliente actualizado", dto);
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<ClienteResponseDTO> noEncontrado = new ApiResponse<>(false, "El cliente con ID: " + id + " no se encontró", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        } catch (BusinessRuleException e) {
            ApiResponse<ClienteResponseDTO> respuestaError = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaError);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<ClienteResponseDTO> respuestaError = new ApiResponse<>(false, "No se pudo actualizar el cliente", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarDatos(@PathVariable Long id) {
        try {
            boolean respuesta = service.eliminarCliente(id);
            if (respuesta) {
                ApiResponse<Void> respuestaExitosa = new ApiResponse<>(true, "El cliente con ID: " + id + " ha sido eliminado");
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<Void> noEncontrado = new ApiResponse<>(false, "El cliente con ID: " + id + " no se encontró");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        } catch (BusinessRuleException e) {

            ApiResponse<Void> conflicto = new ApiResponse<>(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflicto);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<Void> respuestaError = new ApiResponse<>(false, "No se pudo eliminar el cliente");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }
}
