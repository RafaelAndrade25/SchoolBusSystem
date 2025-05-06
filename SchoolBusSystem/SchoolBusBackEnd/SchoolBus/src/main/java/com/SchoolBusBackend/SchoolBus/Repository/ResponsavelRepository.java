package com.SchoolBusBackend.SchoolBus.Repository;

import com.SchoolBusBackend.SchoolBus.model.ResponsavelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponsavelRepository extends JpaRepository<ResponsavelEntity, Long> {
    Optional<ResponsavelEntity> findByCpf(String cpf);
    List<ResponsavelEntity> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT r FROM ResponsavelEntity r WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR r.cpf LIKE CONCAT('%', :termo, '%')")
    List<ResponsavelEntity> buscaPorTermo(String termo);
}
