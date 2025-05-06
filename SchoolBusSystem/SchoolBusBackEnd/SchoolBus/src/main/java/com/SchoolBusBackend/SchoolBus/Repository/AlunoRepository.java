package com.SchoolBusBackend.SchoolBus.Repository;

import com.SchoolBusBackend.SchoolBus.model.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoEntity, Long> {
    Optional<AlunoEntity> findByCpf(String cpf);
    List<AlunoEntity> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT a FROM AlunoEntity a WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR a.cpf LIKE CONCAT('%', :termo, '%')")
    List<AlunoEntity> buscarPorTermo(String termo);

    @Query("SELECT a FROM AlunoEntity a JOIN a.responsaveis r WHERE r.id = :responsavelId")
    List<AlunoEntity> findByResponsavelId(Long responsavelId);
}
