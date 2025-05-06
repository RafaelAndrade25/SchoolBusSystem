package com.SchoolBusBackend.SchoolBus.controller;

import com.SchoolBusBackend.SchoolBus.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {
    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/pagamentos/mes/{mes}/ano/{ano}")
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
