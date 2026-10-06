package com.finalBoss.FinalBoss.modules.clientes.repository;

import com.finalBoss.FinalBoss.modules.clientes.model.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {


    boolean existsByEmail(String email);


    boolean existsByEmailAndIdClienteNot(String email, Long idCliente);
}
