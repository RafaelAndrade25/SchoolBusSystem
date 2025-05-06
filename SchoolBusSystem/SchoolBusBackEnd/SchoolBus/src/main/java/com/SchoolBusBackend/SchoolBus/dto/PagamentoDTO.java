package com.SchoolBusBackend.SchoolBus.dto;

import com.SchoolBusBackend.SchoolBus.model.AlunoEntity;
import com.SchoolBusBackend.SchoolBus.model.PagamentoEntity;
import com.SchoolBusBackend.SchoolBus.model.ResponsavelEntity;
import com.SchoolBusBackend.SchoolBus.model.enums.MetodoPagamento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PagamentoDTO {
    private Long id;
    private Long aluno_id;
    private String aluno_nome;
    private Long responsavel_id;
    private String responsavel_nome;
    private BigDecimal valor;
    private LocalDate dataPagamento;
    private MetodoPagamento metodoPagamento;
    private String status;
    private Integer mesReferencia;
    private Integer anoReferencia;
    private String observacao;

    public static PagamentoDTO fromEntity(PagamentoEntity pagamento){
        if(pagamento == null) return null;
        PagamentoDTO pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setId(pagamento.getId());

        if (pagamento.getAluno() != null) {
            pagamentoDTO.setAluno_id(pagamento.getAluno().getId());
            pagamentoDTO.setAluno_nome(pagamento.getAluno().getNome());
        }

        if (pagamento.getResponsavel() != null) {
            pagamentoDTO.setResponsavel_id(pagamento.getResponsavel().getId());
            pagamentoDTO.setResponsavel_nome(pagamento.getResponsavel().getNome());
        }

        pagamentoDTO.setValor(pagamento.getValor());
        pagamentoDTO.setDataPagamento(pagamento.getDataPagamento());
        pagamentoDTO.setMetodoPagamento(pagamento.getMetodoPagamento());
        pagamentoDTO.setStatus(pagamento.getStatus());
        pagamentoDTO.setMesReferencia(pagamento.getMesReferencia());
        pagamentoDTO.setAnoReferencia(pagamento.getAnoReferencia());
        pagamentoDTO.setObservacao(pagamento.getObservacao());

        return pagamentoDTO;
    }
    public PagamentoEntity toEntity(AlunoEntity aluno, ResponsavelEntity responsavel){
        PagamentoEntity pagamento = new PagamentoEntity();
        pagamento.setId(this.id);
        pagamento.setAluno(aluno);
        pagamento.setResponsavel(responsavel);
        pagamento.setValor(this.valor);
        pagamento.setDataPagamento(this.dataPagamento);
        pagamento.setMetodoPagamento(this.metodoPagamento);
        pagamento.setStatus(this.status);
        pagamento.setMesReferencia(this.mesReferencia);
        pagamento.setAnoReferencia(this.anoReferencia);
        pagamento.setObservacao(this.observacao);

        return pagamento;
    }
}
