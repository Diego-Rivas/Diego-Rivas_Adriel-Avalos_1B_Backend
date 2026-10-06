package com.finalBoss.FinalBoss.modules.eventos.controller;

import com.finalBoss.FinalBoss.exceptions.BusinessRuleException;
import com.finalBoss.FinalBoss.exceptions.DataNotFoundException;
import com.finalBoss.FinalBoss.modules.eventos.model.dto.EventoEstadoDTO;
import com.finalBoss.FinalBoss.modules.eventos.model.dto.EventoRequestDTO;
import com.finalBoss.FinalBoss.modules.eventos.model.dto.EventoResponseDTO;
import com.finalBoss.FinalBoss.modules.eventos.service.EventoService;
import com.finalBoss.FinalBoss.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventoResponseDTO>>> obtenerDatos() {
        try {
            List<EventoResponseDTO> lista = service.obtenerTodos();
            ApiResponse<List<EventoResponseDTO>> respuestaExitosa = new ApiResponse<>(true, "Proceso completado", lista);
            return ResponseEntity.ok(respuestaExitosa);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<List<EventoResponseDTO>> respuestaError = new ApiResponse<>(false, "No se pudieron obtener los eventos", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> obtenerDatosId(@PathVariable Long id) {
        try {
            EventoResponseDTO dto = service.obtenerPorId(id);
            if (dto != null) {
                ApiResponse<EventoResponseDTO> respuestaExitosa = new ApiResponse<>(true, "Evento encontrado", dto);
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<EventoResponseDTO> noEncontrado = new ApiResponse<>(false, "El evento con ID: " + id + " no se encontró", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<EventoResponseDTO> respuestaError = new ApiResponse<>(false, "No se pudo obtener el evento", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventoResponseDTO>> nuevoEvento(@Valid @RequestBody EventoRequestDTO json) {
        try {
            EventoResponseDTO dto = service.nuevoEvento(json);
            ApiResponse<EventoResponseDTO> respuesta = new ApiResponse<>(true,
                    "Evento registrado. Total a pagar: $" + dto.getTotalPago(), dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (DataNotFoundException e) {
            ApiResponse<EventoResponseDTO> respuestaError = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaError);
        } catch (BusinessRuleException e) {
            ApiResponse<EventoResponseDTO> respuestaError = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaError);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<EventoResponseDTO> respuestaError = new ApiResponse<>(false, "No se pudo registrar el evento", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> actualizarEvento(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequestDTO json) {
        try {
            EventoResponseDTO dto = service.actualizarEvento(json, id);
            if (dto != null) {
                ApiResponse<EventoResponseDTO> respuestaExitosa = new ApiResponse<>(true,
                        "Evento actualizado. Total a pagar: $" + dto.getTotalPago(), dto);
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<EventoResponseDTO> noEncontrado = new ApiResponse<>(false, "El evento con ID: " + id + " no se encontró", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        } catch (DataNotFoundException e) {
            ApiResponse<EventoResponseDTO> respuestaError = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaError);
        } catch (BusinessRuleException e) {
            ApiResponse<EventoResponseDTO> respuestaError = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaError);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<EventoResponseDTO> respuestaError = new ApiResponse<>(false, "No se pudo actualizar el evento", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<EventoResponseDTO>> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EventoEstadoDTO json) {
        try {
            EventoResponseDTO dto = service.cambiarEstado(id, json.getEstado());
            if (dto != null) {
                ApiResponse<EventoResponseDTO> respuestaExitosa = new ApiResponse<>(true, "Estado actualizado a " + dto.getEstado(), dto);
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<EventoResponseDTO> noEncontrado = new ApiResponse<>(false, "El evento con ID: " + id + " no se encontró", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        } catch (BusinessRuleException e) {
            ApiResponse<EventoResponseDTO> respuestaError = new ApiResponse<>(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaError);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<EventoResponseDTO> respuestaError = new ApiResponse<>(false, "No se pudo cambiar el estado", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarDatos(@PathVariable Long id) {
        try {
            boolean respuesta = service.eliminarEvento(id);
            if (respuesta) {
                ApiResponse<Void> respuestaExitosa = new ApiResponse<>(true, "El evento con ID: " + id + " ha sido eliminado");
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<Void> noEncontrado = new ApiResponse<>(false, "El evento con ID: " + id + " no se encontró");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<Void> respuestaError = new ApiResponse<>(false, "No se pudo eliminar el evento");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }
}
