package com.finalBoss.FinalBoss.modules.eventos.model.entity;

import com.finalBoss.FinalBoss.modules.clientes.model.entity.ClienteEntity;
import com.finalBoss.FinalBoss.modules.salones.model.entity.SalonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "EVENTOS")
@Getter @Setter @ToString
public class EventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EVENTO")
    private Long idEvento;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE")
    @ToString.Exclude
    private ClienteEntity cliente;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SALON")
    @ToString.Exclude
    private SalonEntity salon;

    @Column(name = "NOMBRE_EVENTO", nullable = false, length = 100)
    private String nombreEvento;

    @Column(name = "FECHA_EVENTO", nullable = false)
    private LocalDate fechaEvento;

    @Column(name = "CANTIDAD_PERSONAS", nullable = false)
    private Integer cantidadPersonas;

    @Column(name = "CANTIDAD_HORAS")
    private Integer cantidadHoras;

    @Column(name = "ESTADO", length = 20)
    private String estado;

    @Column(name = "TOTAL_PAGO")
    private Double totalPago;
}
