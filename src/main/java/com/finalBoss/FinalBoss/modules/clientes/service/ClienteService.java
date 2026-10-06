package com.finalBoss.FinalBoss.modules.clientes.service;

import com.finalBoss.FinalBoss.exceptions.BusinessRuleException;
import com.finalBoss.FinalBoss.modules.clientes.model.dto.ClienteRequestDTO;
import com.finalBoss.FinalBoss.modules.clientes.model.dto.ClienteResponseDTO;
import com.finalBoss.FinalBoss.modules.clientes.model.entity.ClienteEntity;
import com.finalBoss.FinalBoss.modules.clientes.repository.ClienteRepository;
import com.finalBoss.FinalBoss.modules.eventos.repository.EventoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClienteService {

    private final ClienteRepository repo;
    private final EventoRepository repoEvento;

    public ClienteService(ClienteRepository repo, EventoRepository repoEvento) {
        this.repo = repo;
        this.repoEvento = repoEvento;
    }


    public List<ClienteResponseDTO> obtenerTodos() {
        List<ClienteEntity> entidades = repo.findAll();
        List<ClienteResponseDTO> dtos = new ArrayList<>();
        for (ClienteEntity entity : entidades) {
            dtos.add(convertirADTO(entity));
        }
        return dtos;
    }

    public ClienteResponseDTO obtenerPorId(Long id) {
        Optional<ClienteEntity> registro = repo.findById(id);
        if (registro.isPresent()) {
            return convertirADTO(registro.get());
        }
        return null;
    }


    public ClienteResponseDTO nuevoCliente(ClienteRequestDTO dto) {
        if (repo.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("El email " + dto.getEmail() + " ya está registrado");
        }
        ClienteEntity datosConvertidos = convertirAEntity(dto);
        ClienteEntity respuesta = repo.save(datosConvertidos);
        return convertirADTO(respuesta);
    }


    public ClienteResponseDTO actualizarCliente(ClienteRequestDTO dto, Long id) {
        Optional<ClienteEntity> registro = repo.findById(id);
        if (registro.isPresent()) {
            if (repo.existsByEmailAndIdClienteNot(dto.getEmail(), id)) {
                throw new BusinessRuleException("El email " + dto.getEmail() + " ya está registrado en otro cliente");
            }
            ClienteEntity entidad = registro.get();
            entidad.setNombre(dto.getNombre());
            entidad.setApellido(dto.getApellido());
            entidad.setTelefono(dto.getTelefono());
            entidad.setEmail(dto.getEmail());
            entidad.setDireccion(dto.getDireccion());
            ClienteEntity datosGuardados = repo.save(entidad);
            return convertirADTO(datosGuardados);
        }
        return null;
    }


    public boolean eliminarCliente(Long id) {
        if (!repo.existsById(id)) {
            return false;
        }
        if (repoEvento.existsByCliente_IdCliente(id)) {
            throw new BusinessRuleException("No se puede eliminar el cliente porque tiene eventos registrados");
        }
        repo.deleteById(id);
        return true;
    }

    private ClienteEntity convertirAEntity(ClienteRequestDTO dto) {
        ClienteEntity entity = new ClienteEntity();
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setTelefono(dto.getTelefono());
        entity.setEmail(dto.getEmail());
        entity.setDireccion(dto.getDireccion());
        return entity;
    }

    private ClienteResponseDTO convertirADTO(ClienteEntity entity) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setIdCliente(entity.getIdCliente());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setTelefono(entity.getTelefono());
        dto.setEmail(entity.getEmail());
        dto.setDireccion(entity.getDireccion());
        return dto;
    }
}
