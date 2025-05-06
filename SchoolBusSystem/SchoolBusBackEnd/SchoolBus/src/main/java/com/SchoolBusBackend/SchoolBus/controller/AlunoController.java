package com.SchoolBusBackend.SchoolBus.controller;

import com.SchoolBusBackend.SchoolBus.dto.AlunoDTO;
import com.SchoolBusBackend.SchoolBus.model.AlunoEntity;
import com.SchoolBusBackend.SchoolBus.model.ResponsavelEntity;
import com.SchoolBusBackend.SchoolBus.service.AlunoService;
import com.SchoolBusBackend.SchoolBus.service.ResponsavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ResponsavelService responsavelService;

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listarTodos() {
        List<AlunoDTO> alunos = alunoService.listarTodos().stream()
                .map(AlunoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id)
                .map(AlunoDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
    }

    @GetMapping("/busca")
    public ResponseEntity<List<AlunoDTO>> buscarPorTermo(@RequestParam String termo) {
        List<AlunoDTO> alunos = alunoService.buscarPorTermo(termo).stream()
                .map(AlunoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/responsavel/{responsavelId}")
    public ResponseEntity<List<AlunoDTO>> buscarPorResponsavel(@PathVariable Long responsavelId) {
        List<AlunoDTO> alunos = alunoService.buscarPorResponsavel(responsavelId).stream()
                .map(AlunoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alunos);
    }

    @PostMapping
    public ResponseEntity<AlunoDTO> criar(@RequestBody AlunoDTO alunoDTO) {
        try{
            AlunoEntity aluno = alunoDTO.toEntity();

            List<ResponsavelEntity> responsaveis = new ArrayList<>();
            if(alunoDTO.getResponsaveis() != null){
                for(var respDTO : alunoDTO.getResponsaveis()){
                    responsavelService.buscarPorId(respDTO.getId())
                            .ifPresent(responsaveis::add);
                }
            }
            aluno.setResponsaveis(responsaveis);
            AlunoEntity salvo = alunoService.salvar(aluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(AlunoDTO.fromEntity(salvo));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoDTO> atualizar(@PathVariable Long id, @RequestBody AlunoDTO alunoDTO) {
        if (!alunoService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado");
        }

        try {
            AlunoEntity aluno = alunoDTO.toEntity();
            aluno.setId(id);

            // Manter responsáveis existentes ou atualizar
            AlunoEntity existente = alunoService.buscarPorId(id).get();

            // Se não enviou responsáveis, manter os existentes
            if (alunoDTO.getResponsaveis() == null || alunoDTO.getResponsaveis().isEmpty()) {
                aluno.setResponsaveis(existente.getResponsaveis());
            } else {
                // Atualizar responsáveis
                List<ResponsavelEntity> responsaveis = new ArrayList<>();
                for (var respDTO : alunoDTO.getResponsaveis()) {
                    responsavelService.buscarPorId(respDTO.getId())
                            .ifPresent(responsaveis::add);
                }
                aluno.setResponsaveis(responsaveis);
            }

            // Manter foto se não enviou nova
            if (aluno.getImagem_aluno() == null || aluno.getImagem_aluno().length == 0) {
                aluno.setImagem_aluno(existente.getImagem_aluno());
            }

            AlunoEntity atualizado = alunoService.salvar(aluno);
            return ResponseEntity.ok(AlunoDTO.fromEntity(atualizado));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!alunoService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado");
        }
        alunoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
