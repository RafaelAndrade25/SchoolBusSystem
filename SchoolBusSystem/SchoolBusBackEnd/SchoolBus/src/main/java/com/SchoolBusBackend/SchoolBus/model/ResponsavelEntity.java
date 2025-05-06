package com.SchoolBusBackend.SchoolBus.model;

import com.SchoolBusBackend.SchoolBus.model.enums.TipoResponsavel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ResponsavelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String email;
    private String telefone;

    @Enumerated(EnumType.STRING)
    private TipoResponsavel tipoResponsavel;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    private List<EnderecoEntity> enderecos = new ArrayList<>();

    @ManyToMany(mappedBy = "responsaveis")
    private List<AlunoEntity> alunos = new ArrayList<>();
}
