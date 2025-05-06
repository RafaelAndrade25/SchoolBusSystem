package com.SchoolBusBackend.SchoolBus.dto;

import com.SchoolBusBackend.SchoolBus.model.AlunoEntity;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AlunoDTO {
    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String alergias;
    private String doencas;
    private List<ResponsavelDTO> responsaveis = new ArrayList<>();
    private String fotoBase64;

    public static AlunoDTO fromEntity(AlunoEntity aluno) {
        if (aluno == null) return null;

        AlunoDTO alunoDTO = new AlunoDTO();
        alunoDTO.setId(aluno.getId());
        alunoDTO.setNome(aluno.getNome());
        alunoDTO.setCpf(aluno.getCpf());
        alunoDTO.setDataNascimento(aluno.getDataNascimento());
        alunoDTO.setAlergias(aluno.getAlergias());
        alunoDTO.setDoencas(aluno.getDoencas());

        if (aluno.getResponsaveis() != null) {
           alunoDTO.setResponsaveis(
                   aluno.getResponsaveis().stream()
                           .map(ResponsavelDTO::fromEntity)
                           .collect(Collectors.toList())
           );
        }

        if (aluno.getImagem_aluno() != null) {
            alunoDTO.setFotoBase64(java.util.Base64.getEncoder().encodeToString(aluno.getImagem_aluno()));
        }

        return alunoDTO;
    }
    public AlunoEntity toEntity() {
        AlunoEntity aluno = new AlunoEntity();
        aluno.setId(this.id);
        aluno.setNome(this.nome);
        aluno.setCpf(this.cpf);
        aluno.setDataNascimento(this.dataNascimento);
        aluno.setAlergias(this.alergias);
        aluno.setDoencas(this.doencas);

        if (this.fotoBase64 != null && !this.fotoBase64.isEmpty()) {
            aluno.setImagem_aluno(java.util.Base64.getDecoder().decode(this.fotoBase64));
        }

        return aluno;
    }
}
