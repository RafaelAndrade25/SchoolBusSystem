package com.SchoolBusBackend.SchoolBus.dto;

import com.SchoolBusBackend.SchoolBus.model.ResponsavelEntity;
import com.SchoolBusBackend.SchoolBus.model.enums.TipoResponsavel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResponsavelDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private TipoResponsavel tipoResponsavel;
    private List<EnderecoDTO> enderecos = new ArrayList<>();

    public static ResponsavelDTO fromEntity(ResponsavelEntity responsavelEntity){
        if(responsavelEntity == null) return null;

        ResponsavelDTO responsavelDTO = new ResponsavelDTO();
        responsavelDTO.setId(responsavelEntity.getId());
        responsavelDTO.setNome(responsavelEntity.getNome());
        responsavelDTO.setCpf(responsavelEntity.getCpf());
        responsavelDTO.setEmail(responsavelEntity.getEmail());
        responsavelDTO.setTelefone(responsavelEntity.getTelefone());
        responsavelDTO.setTipoResponsavel(responsavelEntity.getTipoResponsavel());

        if(responsavelEntity.getEnderecos()!=null){
            responsavelDTO.setEnderecos(
                    responsavelEntity.getEnderecos().stream()
                            .map(EnderecoDTO::fromEntity)
                            .collect(Collectors.toList())
            );
        }
        return responsavelDTO;
    }
    public ResponsavelEntity toEntity(){
        ResponsavelEntity responsavelEntity = new ResponsavelEntity();
        responsavelEntity.setId(this.id);
        responsavelEntity.setNome(this.nome);
        responsavelEntity.setCpf(this.cpf);
        responsavelEntity.setEmail(this.email);
        responsavelEntity.setTelefone(this.telefone);
        responsavelEntity.setTipoResponsavel(this.tipoResponsavel);

        if(this.enderecos!=null){
            responsavelEntity.setEnderecos(
                    this.enderecos.stream()
                            .map(EnderecoDTO::toEntity)
                    .collect(Collectors.toList())
            );
        }
        return responsavelEntity;
    }
}
