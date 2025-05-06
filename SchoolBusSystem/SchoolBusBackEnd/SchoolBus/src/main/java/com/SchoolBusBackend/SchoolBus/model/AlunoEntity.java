package com.SchoolBusBackend.SchoolBus.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class AlunoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String alergias;
    private String doencas;

    @Lob
    private byte[] imagem_aluno;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "responsavel_aluno",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "responsavel_id")
    )
    private List<ResponsavelEntity> responsaveis = new ArrayList<>();
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<PagamentoEntity> pagamentos = new ArrayList<>();
}
