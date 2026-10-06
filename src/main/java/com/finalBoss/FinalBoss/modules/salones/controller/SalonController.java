package com.finalBoss.FinalBoss.modules.salones.controller;

import com.finalBoss.FinalBoss.modules.salones.model.dto.SalonRequestDTO;
import com.finalBoss.FinalBoss.modules.salones.model.dto.SalonResponseDTO;
import com.finalBoss.FinalBoss.modules.salones.service.SalonService;
import com.finalBoss.FinalBoss.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/salones")
@CrossOrigin(origins = "*")
public class SalonController {

    private final SalonService service;

    public SalonController(SalonService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<SalonResponseDTO>>> obtenerDatos() {
        try {
            List<SalonResponseDTO> lista = service.obtenerTodos();
            ApiResponse<List<SalonResponseDTO>> respuestaExitosa = new ApiResponse<>(true, "Proceso completado", lista);
            return ResponseEntity.ok(respuestaExitosa);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<List<SalonResponseDTO>> respuestaError = new ApiResponse<>(false, "No se pudieron obtener los salones", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalonResponseDTO>> obtenerDatosId(@PathVariable Long id) {
        try {
            SalonResponseDTO dto = service.obtenerPorId(id);
            if (dto != null) {
                ApiResponse<SalonResponseDTO> respuestaExitosa = new ApiResponse<>(true, "Salón encontrado", dto);
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<SalonResponseDTO> noEncontrado = new ApiResponse<>(false, "El salón con ID: " + id + " no se encontró", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<SalonResponseDTO> respuestaError = new ApiResponse<>(false, "No se pudo obtener el salón", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }


    @PostMapping
    public ResponseEntity<ApiResponse<SalonResponseDTO>> nuevoSalon(@Valid @RequestBody SalonRequestDTO json) {
        try {
            SalonResponseDTO dto = service.nuevoSalon(json);
            ApiResponse<SalonResponseDTO> respuesta = new ApiResponse<>(true, "Salón registrado", dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<SalonResponseDTO> respuestaError = new ApiResponse<>(false, "No se pudo registrar el salón", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

}
