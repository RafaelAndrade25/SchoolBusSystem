package com.SchoolBusBackend.SchoolBus.model;

import com.SchoolBusBackend.SchoolBus.model.enums.MetodoPagamento;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class PagamentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private AlunoEntity aluno;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private ResponsavelEntity responsavel;

    private BigDecimal valor;
    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    private String status;
    private Integer mesReferencia;
    private Integer anoReferencia;
    private String observacao;

}
