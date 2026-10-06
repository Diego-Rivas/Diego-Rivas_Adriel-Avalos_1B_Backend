package com.finalBoss.FinalBoss.modules.salones.service;

import com.finalBoss.FinalBoss.modules.salones.model.dto.SalonRequestDTO;
import com.finalBoss.FinalBoss.modules.salones.model.dto.SalonResponseDTO;
import com.finalBoss.FinalBoss.modules.salones.model.entity.SalonEntity;
import com.finalBoss.FinalBoss.modules.salones.repository.SalonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SalonService {

    private final SalonRepository repo;

    public SalonService(SalonRepository repo) {
        this.repo = repo;
    }


    public List<SalonResponseDTO> obtenerTodos() {
        List<SalonEntity> entidades = repo.findAll();
        List<SalonResponseDTO> dtos = new ArrayList<>();
        for (SalonEntity entity : entidades) {
            dtos.add(convertirADTO(entity));
        }
        return dtos;
    }


    public SalonResponseDTO obtenerPorId(Long id) {
        Optional<SalonEntity> registro = repo.findById(id);
        if (registro.isPresent()) {
            return convertirADTO(registro.get());
        }
        return null;
    }


    public SalonResponseDTO nuevoSalon(SalonRequestDTO dto) {
        SalonEntity datosConvertidos = convertirAEntity(dto);
        SalonEntity respuesta = repo.save(datosConvertidos);
        return convertirADTO(respuesta);
    }


    public boolean eliminarSalon(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    private SalonEntity convertirAEntity(SalonRequestDTO dto) {
        SalonEntity entity = new SalonEntity();
        // El id NO se copia: lo genera Oracle
        entity.setNombreSalon(dto.getNombreSalon());
        entity.setCapacidad(dto.getCapacidad());
        entity.setPrecioRenta(dto.getPrecioRenta());
        entity.setUbicacion(dto.getUbicacion());
        return entity;
    }

    private SalonResponseDTO convertirADTO(SalonEntity entity) {
        SalonResponseDTO dto = new SalonResponseDTO();
        dto.setIdSalon(entity.getIdSalon());
        dto.setNombreSalon(entity.getNombreSalon());
        dto.setCapacidad(entity.getCapacidad());
        dto.setPrecioRenta(entity.getPrecioRenta());
        dto.setUbicacion(entity.getUbicacion());
        return dto;
    }
}
