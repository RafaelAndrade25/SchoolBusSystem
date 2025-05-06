// PagamentoController.java
package com.vanescolar.controller;

import com.SchoolBusBackend.SchoolBus.dto.PagamentoDTO;
import com.SchoolBusBackend.SchoolBus.model.AlunoEntity;
import com.SchoolBusBackend.SchoolBus.model.PagamentoEntity;
import com.SchoolBusBackend.SchoolBus.model.ResponsavelEntity;
import com.SchoolBusBackend.SchoolBus.service.AlunoService;
import com.SchoolBusBackend.SchoolBus.service.PagamentoService;
import com.SchoolBusBackend.SchoolBus.service.RelatorioService;
import com.SchoolBusBackend.SchoolBus.service.ResponsavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pagamentos")
@CrossOrigin(origins = "*")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ResponsavelService responsavelService;

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping
    public ResponseEntity<List<PagamentoDTO>> listarTodos() {
        List<PagamentoDTO> pagamentos = pagamentoService.listarTodos().stream()
                .map(PagamentoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> buscarPorId(@PathVariable Long id) {
        return pagamentoService.buscarPorId(id)
                .map(PagamentoDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagamento não encontrado"));
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<PagamentoDTO>> buscarPorAluno(@PathVariable Long alunoId) {
        List<PagamentoDTO> pagamentos = pagamentoService.buscarPorAluno(alunoId).stream()
                .map(PagamentoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/responsavel/{responsavelId}")
    public ResponseEntity<List<PagamentoDTO>> buscarPorResponsavel(@PathVariable Long responsavelId) {
        List<PagamentoDTO> pagamentos = pagamentoService.buscarPorResponsavel(responsavelId).stream()
                .map(PagamentoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/mes/{mes}/ano/{ano}")
    public ResponseEntity<List<PagamentoDTO>> buscarPorMesAno(@PathVariable Integer mes, @PathVariable Integer ano) {
        List<PagamentoDTO> pagamentos = pagamentoService.buscarPorMesAno(mes, ano).stream()
                .map(PagamentoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pagamentos);
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> criar(@RequestBody PagamentoDTO pagamentoDTO) {
        try {
            AlunoEntity aluno = alunoService.buscarPorId(pagamentoDTO.getAluno_id())
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

            ResponsavelEntity responsavel = responsavelService.buscarPorId(pagamentoDTO.getResponsavel_id())
                    .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado"));

            PagamentoEntity pagamento = pagamentoDTO.toEntity(aluno, responsavel);
            PagamentoEntity salvo = pagamentoService.salvar(pagamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(PagamentoDTO.fromEntity(salvo));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> atualizar(@PathVariable Long id, @RequestBody PagamentoDTO pagamentoDTO) {
        if (!pagamentoService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagamento não encontrado");
        }

        try {
            AlunoEntity aluno = alunoService.buscarPorId(pagamentoDTO.getAluno_id())
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

            ResponsavelEntity responsavel = responsavelService.buscarPorId(pagamentoDTO.getResponsavel_id())
                    .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado"));

            PagamentoEntity pagamento = pagamentoDTO.toEntity(aluno, responsavel);
            pagamento.setId(id);

            PagamentoEntity atualizado = pagamentoService.salvar(pagamento);
            return ResponseEntity.ok(PagamentoDTO.fromEntity(atualizado));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!pagamentoService.buscarPorId(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagamento não encontrado");
        }
        pagamentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/relatorio/mes/{mes}/ano/{ano}")
    public ResponseEntity<byte[]> gerarRelatorioPagamentosMensal(@PathVariable Integer mes, @PathVariable Integer ano) {
        try {
            byte[] relatorio = relatorioService.gerarRelatorioPagamentosMensal(mes, ano);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = String.format("relatorio-pagamentos-%d-%d.pdf", mes, ano);
            headers.setContentDispositionFormData("filename", filename);

            return new ResponseEntity<>(relatorio, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao gerar relatório: " + e.getMessage());
        }
    }
}