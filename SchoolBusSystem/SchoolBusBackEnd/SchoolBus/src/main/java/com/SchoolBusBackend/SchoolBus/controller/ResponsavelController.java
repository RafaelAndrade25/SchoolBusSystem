package com.SchoolBusBackend.SchoolBus.controller;

import com.SchoolBusBackend.SchoolBus.dto.ResponsavelDTO;
import com.SchoolBusBackend.SchoolBus.model.ResponsavelEntity;
import com.SchoolBusBackend.SchoolBus.service.ResponsavelService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/responsaveis")
@CrossOrigin(origins = "*")
public class ResponsavelController {
    @Autowired
    private ResponsavelService responsavelService;

    @GetMapping
    public ResponseEntity<List<ResponsavelDTO>> listarTodos() {
        List<ResponsavelDTO> responsaveis = responsavelService.listarTodos().stream()
                .map(ResponsavelDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responsaveis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsavelDTO> buscarPorId(@PathVariable Long id) {
        return responsavelService.buscarPorId(id)
                .map(ResponsavelDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsável não encontrado"));
    }

    @GetMapping("/busca")
    public ResponseEntity<List<ResponsavelDTO>> buscarPorNTermo(@RequestParam String termo) {
        List<ResponsavelDTO> responsaveis = responsavelService.buscarPorTermo(termo).stream()
                .map(ResponsavelDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responsaveis);
    }

    @PostMapping
    public ResponseEntity<ResponsavelDTO> criar(@RequestBody ResponsavelDTO responsavelDTO){
        try{
            ResponsavelEntity responsavel = responsavelDTO.toEntity();
            ResponsavelEntity salvo = responsavelService.salvar(responsavel);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponsavelDTO.fromEntity(salvo));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponsavelDTO> atualizar(@PathVariable Long id, @RequestBody ResponsavelDTO responsavelDTO) {
        if (!responsavelService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsável não encontrado");
        }

        try {
            ResponsavelEntity responsavel = responsavelDTO.toEntity();
            responsavel.setId(id);
            ResponsavelEntity atualizado = responsavelService.salvar(responsavel);
            return ResponseEntity.ok(ResponsavelDTO.fromEntity(atualizado));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!responsavelService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsável não encontrado");
        }
        responsavelService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
