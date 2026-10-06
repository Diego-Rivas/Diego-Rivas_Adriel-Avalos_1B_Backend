package com.finalBoss.FinalBoss.modules.eventos.service;

import com.finalBoss.FinalBoss.exceptions.BusinessRuleException;
import com.finalBoss.FinalBoss.exceptions.DataNotFoundException;
import com.finalBoss.FinalBoss.modules.clientes.model.entity.ClienteEntity;
import com.finalBoss.FinalBoss.modules.clientes.repository.ClienteRepository;
import com.finalBoss.FinalBoss.modules.eventos.model.dto.EventoRequestDTO;
import com.finalBoss.FinalBoss.modules.eventos.model.dto.EventoResponseDTO;
import com.finalBoss.FinalBoss.modules.eventos.model.entity.EventoEntity;
import com.finalBoss.FinalBoss.modules.eventos.repository.EventoRepository;
import com.finalBoss.FinalBoss.modules.salones.model.entity.SalonEntity;
import com.finalBoss.FinalBoss.modules.salones.repository.SalonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EventoService {


    private static final String ESTADO_INICIAL = "CONFIRMADO";


    private static final List<String> ESTADOS_ACTIVOS = List.of("PENDIENTE", "CONFIRMADO");

    private final EventoRepository repo;
    private final ClienteRepository repoCliente;
    private final SalonRepository repoSalon;

    public EventoService(EventoRepository repo, ClienteRepository repoCliente, SalonRepository repoSalon) {
        this.repo = repo;
        this.repoCliente = repoCliente;
        this.repoSalon = repoSalon;
    }


    public List<EventoResponseDTO> obtenerTodos() {
        List<EventoEntity> entidades = repo.findAll();
        List<EventoResponseDTO> dtos = new ArrayList<>();
        for (EventoEntity entity : entidades) {
            dtos.add(convertirADTO(entity));
        }
        return dtos;
    }


    public EventoResponseDTO obtenerPorId(Long id) {
        Optional<EventoEntity> registro = repo.findById(id);
        if (registro.isPresent()) {
            return convertirADTO(registro.get());
        }
        return null;
    }


    public EventoResponseDTO nuevoEvento(EventoRequestDTO dto) {
        ClienteEntity cliente = buscarCliente(dto.getIdCliente());
        SalonEntity salon = buscarSalon(dto.getIdSalon());

        validarCapacidad(dto.getCantidadPersonas(), salon);

        if (repo.existsBySalon_IdSalonAndFechaEventoAndEstadoIn(salon.getIdSalon(), dto.getFechaEvento(), ESTADOS_ACTIVOS)) {
            throw new BusinessRuleException("El salón " + salon.getNombreSalon()
                    + " ya tiene un evento activo el " + dto.getFechaEvento());
        }


        EventoEntity entity = convertirAEntity(dto, cliente, salon);
        entity.setEstado(ESTADO_INICIAL);
        entity.setTotalPago(calcularTotal(dto.getCantidadHoras(), salon));

        EventoEntity respuesta = repo.save(entity);
        return convertirADTO(respuesta);
    }

    public EventoResponseDTO actualizarEvento(EventoRequestDTO dto, Long id) {
        Optional<EventoEntity> registro = repo.findById(id);
        if (registro.isPresent()) {
            ClienteEntity cliente = buscarCliente(dto.getIdCliente());
            SalonEntity salon = buscarSalon(dto.getIdSalon());

            validarCapacidad(dto.getCantidadPersonas(), salon);
            if (repo.existsBySalon_IdSalonAndFechaEventoAndEstadoInAndIdEventoNot(
                    salon.getIdSalon(), dto.getFechaEvento(), ESTADOS_ACTIVOS, id)) {
                throw new BusinessRuleException("El salón " + salon.getNombreSalon()
                        + " ya tiene un evento activo el " + dto.getFechaEvento());
            }

            EventoEntity entidad = registro.get();
            entidad.setCliente(cliente);
            entidad.setSalon(salon);
            entidad.setNombreEvento(dto.getNombreEvento());
            entidad.setFechaEvento(dto.getFechaEvento());
            entidad.setCantidadPersonas(dto.getCantidadPersonas());
            entidad.setCantidadHoras(dto.getCantidadHoras());
            entidad.setTotalPago(calcularTotal(dto.getCantidadHoras(), salon));   // se vuelve a calcular
            EventoEntity datosGuardados = repo.save(entidad);
            return convertirADTO(datosGuardados);
        }
        return null;
    }


    public EventoResponseDTO cambiarEstado(Long id, String nuevoEstado) {
        Optional<EventoEntity> registro = repo.findById(id);
        if (registro.isPresent()) {
            EventoEntity entidad = registro.get();

            if (ESTADOS_ACTIVOS.contains(nuevoEstado)
                    && repo.existsBySalon_IdSalonAndFechaEventoAndEstadoInAndIdEventoNot(
                    entidad.getSalon().getIdSalon(), entidad.getFechaEvento(), ESTADOS_ACTIVOS, id)) {
                throw new BusinessRuleException("No se puede activar: el salón ya tiene otro evento activo ese día");
            }
            entidad.setEstado(nuevoEstado);
            EventoEntity datosGuardados = repo.save(entidad);
            return convertirADTO(datosGuardados);
        }
        return null;
    }

    public boolean eliminarEvento(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    private ClienteEntity buscarCliente(Long idCliente) {
        return repoCliente.findById(idCliente)
                .orElseThrow(() -> new DataNotFoundException("El cliente con ID: " + idCliente + " no existe"));
    }

    private SalonEntity buscarSalon(Long idSalon) {
        return repoSalon.findById(idSalon)
                .orElseThrow(() -> new DataNotFoundException("El salón con ID: " + idSalon + " no existe"));
    }

    private void validarCapacidad(Integer cantidadPersonas, SalonEntity salon) {
        if (cantidadPersonas > salon.getCapacidad()) {
            throw new BusinessRuleException("La cantidad de personas (" + cantidadPersonas
                    + ") supera la capacidad máxima del salón " + salon.getNombreSalon()
                    + " (" + salon.getCapacidad() + " personas)");
        }
    }


    private Double calcularTotal(Integer cantidadHoras, SalonEntity salon) {
        double total = cantidadHoras * salon.getPrecioRenta();
        return Math.round(total * 100.0) / 100.0;
    }



    private EventoEntity convertirAEntity(EventoRequestDTO dto, ClienteEntity cliente, SalonEntity salon) {
        EventoEntity entity = new EventoEntity();
        entity.setCliente(cliente);
        entity.setSalon(salon);
        entity.setNombreEvento(dto.getNombreEvento());
        entity.setFechaEvento(dto.getFechaEvento());
        entity.setCantidadPersonas(dto.getCantidadPersonas());
        entity.setCantidadHoras(dto.getCantidadHoras());
        return entity;
    }

    private EventoResponseDTO convertirADTO(EventoEntity entity) {
        EventoResponseDTO dto = new EventoResponseDTO();
        dto.setIdEvento(entity.getIdEvento());

        dto.setIdCliente(entity.getCliente().getIdCliente());
        dto.setNombreCliente(entity.getCliente().getNombre() + " " + entity.getCliente().getApellido());

        dto.setIdSalon(entity.getSalon().getIdSalon());
        dto.setNombreSalon(entity.getSalon().getNombreSalon());
        dto.setPrecioRenta(entity.getSalon().getPrecioRenta());

        dto.setNombreEvento(entity.getNombreEvento());
        dto.setFechaEvento(entity.getFechaEvento());
        dto.setCantidadPersonas(entity.getCantidadPersonas());
        dto.setCantidadHoras(entity.getCantidadHoras());
        dto.setEstado(entity.getEstado());
        dto.setTotalPago(entity.getTotalPago());
        return dto;
    }
}
