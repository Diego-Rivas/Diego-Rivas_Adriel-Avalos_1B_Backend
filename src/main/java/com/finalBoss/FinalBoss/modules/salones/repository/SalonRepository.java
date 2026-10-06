package com.finalBoss.FinalBoss.modules.salones.repository;

import com.finalBoss.FinalBoss.modules.salones.model.entity.SalonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalonRepository extends JpaRepository<SalonEntity, Long> {
}
