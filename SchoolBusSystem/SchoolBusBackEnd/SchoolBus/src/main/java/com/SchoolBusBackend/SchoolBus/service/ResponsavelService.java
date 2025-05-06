package com.SchoolBusBackend.SchoolBus.service;

import com.SchoolBusBackend.SchoolBus.Repository.ResponsavelRepository;
import com.SchoolBusBackend.SchoolBus.model.ResponsavelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponsavelService {
    @Autowired
    private ResponsavelRepository responsavelRepository;

    public List<ResponsavelEntity> listarTodos(){
        return responsavelRepository.findAll();
    }

    public Optional<ResponsavelEntity> buscarPorId(Long id){
        return responsavelRepository.findById(id);
    }

    public Optional<ResponsavelEntity> buscarPorCpf(String cpf){
        return responsavelRepository.findByCpf(cpf);
    }

    public List<ResponsavelEntity> buscarPorNome(String nome){
        return responsavelRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<ResponsavelEntity> buscarPorTermo(String termo){
        return responsavelRepository.buscaPorTermo(termo);
    }

    public ResponsavelEntity salvar(ResponsavelEntity responsavel) {
        //valida se já existe CPF
        Optional<ResponsavelEntity> existente = buscarPorCpf(responsavel.getCpf());
        if (existente.isPresent() && !existente.get().getId().equals(responsavel.getId())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        //Validar Quantidade de endereços
        if (responsavel.getEnderecos() != null && responsavel.getEnderecos().size() > 3) {
            throw new IllegalArgumentException("Máximo de 3 endereços permitidos");
        }
        return responsavelRepository.save(responsavel);
    }
    public void excluir(Long id){
        responsavelRepository.deleteById(id);
    }
}
