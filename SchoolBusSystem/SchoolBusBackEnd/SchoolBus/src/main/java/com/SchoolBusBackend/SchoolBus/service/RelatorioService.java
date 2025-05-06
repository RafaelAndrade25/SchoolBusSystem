package com.SchoolBusBackend.SchoolBus.service;

import com.SchoolBusBackend.SchoolBus.model.PagamentoEntity;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class RelatorioService {
    @Autowired
    private PagamentoService pagamentoService;

    public byte[] gerarRelatorioPagamentosMensal(Integer mes, Integer ano) throws DocumentException{
        List<PagamentoEntity> pagamentos = pagamentoService.buscarPorMesAno(mes, ano);
        ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, bytearray);

        document.open();

        //Cabeçalho
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        Paragraph title = new Paragraph("Relatório de Pagamentos - " + meses[mes - 1] + "/" + ano, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(""));

        //Tabela de pagamentos
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 3, 2, 2, 2, 2});

        table.addCell(new PdfPCell(new Phrase("Aluno", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Responsável", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Valor", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Data", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Método", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Status", headerFont)));

        //Formatadores
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        //Dados da tabela
        for (PagamentoEntity pagamento : pagamentos) {
            table.addCell(new PdfPCell(new Phrase(pagamento.getAluno().getNome(), normalFont)));
            table.addCell(new PdfPCell(new Phrase(pagamento.getResponsavel().getNome(), normalFont)));
            table.addCell(new PdfPCell(new Phrase(currencyFormat.format(pagamento.getValor()), normalFont)));
            table.addCell(new PdfPCell(new Phrase(pagamento.getDataPagamento().format(dateFormatter), normalFont)));
            table.addCell(new PdfPCell(new Phrase(pagamento.getMetodoPagamento().toString(), normalFont)));
            table.addCell(new PdfPCell(new Phrase(pagamento.getStatus(), normalFont)));
        }
        document.add(table);

        //Resumo
        document.add(new Paragraph(""));
        document.add(new Paragraph("Resumo: ", headerFont));
        document.add(new Paragraph("Total de Pagamentos: " + pagamentos.size(), normalFont));

        double total = pagamentos.stream().mapToDouble(pag ->pag.getValor().doubleValue())
                .sum();

        document.add(new Paragraph("Valor Total: "+ currencyFormat.format(total), normalFont));
        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Gerado em: " + LocalDate.now().format(dateFormatter), normalFont);
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);

        document.close();
        return bytearray.toByteArray();

    }
}
