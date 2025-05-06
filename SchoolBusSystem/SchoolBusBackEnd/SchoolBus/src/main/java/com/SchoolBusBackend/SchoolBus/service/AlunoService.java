package com.SchoolBusBackend.SchoolBus.service;

import com.SchoolBusBackend.SchoolBus.Repository.AlunoRepository;
import com.SchoolBusBackend.SchoolBus.model.AlunoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    @Autowired
    private AlunoRepository alunoRepository;

    public List<AlunoEntity> listarTodos(){
        return alunoRepository.findAll();
    }

    public Optional<AlunoEntity> buscarPorId(Long id){
        return alunoRepository.findById(id);
    }

    public Optional<AlunoEntity> buscarPorCpf(String cpf){
        return alunoRepository.findByCpf(cpf);
    }

    public List<AlunoEntity> buscarPorNome(String nome){
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<AlunoEntity> buscarPorTermo(String termo){
        return alunoRepository.buscarPorTermo(termo);
    }

    public List<AlunoEntity> buscarPorResponsavel(Long responsavelId){
        return alunoRepository.findByResponsavelId(responsavelId);
    }

    public AlunoEntity salvar(AlunoEntity aluno){
        //valida se ja existe cpf
        Optional<AlunoEntity> existente = buscarPorCpf(aluno.getCpf());
        if (existente.isPresent() && !existente.get().getId().equals(aluno.getId())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        return alunoRepository.save(aluno);
    }
    public void excluir(Long id) {
        alunoRepository.deleteById(id);
    }
}
