package com.finalBoss.FinalBoss.modules.eventos.repository;

import com.finalBoss.FinalBoss.modules.eventos.model.entity.EventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<EventoEntity, Long> {

    boolean existsBySalon_IdSalonAndFechaEventoAndEstadoIn(Long idSalon, LocalDate fechaEvento, List<String> estados);

    boolean existsBySalon_IdSalonAndFechaEventoAndEstadoInAndIdEventoNot(Long idSalon, LocalDate fechaEvento, List<String> estados, Long idEvento);

    boolean existsByCliente_IdCliente(Long idCliente);
}
