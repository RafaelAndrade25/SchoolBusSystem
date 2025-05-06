package com.SchoolBusBackend.SchoolBus.dto;

import com.SchoolBusBackend.SchoolBus.model.EnderecoEntity;
import lombok.Data;

@Data
public class EnderecoDTO {
    private Long id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String referencia;

    public static EnderecoDTO fromEntity(EnderecoEntity enderecoEntity) {
        if (enderecoEntity == null) return null;

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setId(enderecoEntity.getId());
        enderecoDTO.setLogradouro(enderecoEntity.getLogradouro());
        enderecoDTO.setNumero(enderecoEntity.getNumero());
        enderecoDTO.setComplemento(enderecoEntity.getComplemento());
        enderecoDTO.setBairro(enderecoEntity.getBairro());
        enderecoDTO.setCidade(enderecoEntity.getCidade());
        enderecoDTO.setUf(enderecoEntity.getUf());
        enderecoDTO.setCep(enderecoEntity.getCep());
        enderecoDTO.setReferencia(enderecoEntity.getReferencia());
        return enderecoDTO;
    }

    public EnderecoEntity toEntity() {
        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(this.id);
        enderecoEntity.setLogradouro(this.logradouro);
        enderecoEntity.setNumero(this.numero);
        enderecoEntity.setComplemento(this.complemento);
        enderecoEntity.setBairro(this.bairro);
        enderecoEntity.setCidade(this.cidade);
        enderecoEntity.setUf(this.uf);
        enderecoEntity.setCep(this.cep);
        enderecoEntity.setReferencia(this.referencia);
        return enderecoEntity;
    }
}
