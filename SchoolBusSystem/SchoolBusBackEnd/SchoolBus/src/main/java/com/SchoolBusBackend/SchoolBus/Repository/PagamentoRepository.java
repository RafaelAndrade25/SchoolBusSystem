package com.SchoolBusBackend.SchoolBus.Repository;

import com.SchoolBusBackend.SchoolBus.model.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<PagamentoEntity, Long> {
    List<PagamentoEntity> findByAlunoId(Long alunoId);
    List<PagamentoEntity> findByResponsavelId(Long responsavelId);
    List<PagamentoEntity> findByMesReferenciaAndAnoReferencia(Integer mes, Integer ano);
    List<PagamentoEntity> findByAnoAlunoIdAndMesReferenciaAndAnoReferencia(Long alunoId, Integer mes, Integer ano);
}
