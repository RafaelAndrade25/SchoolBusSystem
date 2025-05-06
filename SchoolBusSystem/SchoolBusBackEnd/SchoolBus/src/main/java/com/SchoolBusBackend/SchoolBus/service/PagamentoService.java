package com.SchoolBusBackend.SchoolBus.service;

import com.SchoolBusBackend.SchoolBus.Repository.PagamentoRepository;
import com.SchoolBusBackend.SchoolBus.model.PagamentoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    public List<PagamentoEntity> listarTodos(){
        return pagamentoRepository.findAll();
    }

    public Optional<PagamentoEntity> buscarPorId(Long id){
        return pagamentoRepository.findById(id);
    }

    public List<PagamentoEntity> buscarPorAluno(Long alunoid){
        return pagamentoRepository.findByAlunoId(alunoid);
    }
    public List<PagamentoEntity> buscarPorResponsavel(Long responsavelId) {
        return pagamentoRepository.findByResponsavelId(responsavelId);
    }

    public List<PagamentoEntity> buscarPorMesAno(Integer mes, Integer ano) {
        return pagamentoRepository.findByMesReferenciaAndAnoReferencia(mes, ano);
    }

    public List<PagamentoEntity> buscarPorAlunoMesAno(Long alunoId, Integer mes, Integer ano) {
        return pagamentoRepository.findByAnoAlunoIdAndMesReferenciaAndAnoReferencia(alunoId, mes, ano);
    }

    public PagamentoEntity salvar(PagamentoEntity pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    public void excluir(Long id) {
        pagamentoRepository.deleteById(id);
    }
}
