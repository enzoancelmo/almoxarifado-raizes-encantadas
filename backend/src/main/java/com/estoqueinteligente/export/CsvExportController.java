package com.estoqueinteligente.export;

import com.estoqueinteligente.stockmovement.StockMovementType;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exports")
public class CsvExportController {
    private final CsvExportService service;
    public CsvExportController(CsvExportService service){this.service=service;}
    @GetMapping(value="/items.csv",produces="text/csv;charset=UTF-8") public ResponseEntity<String> items(){return csv("itens.csv",service.itemsCsv());}
    @GetMapping(value="/movements.csv",produces="text/csv;charset=UTF-8") public ResponseEntity<String> movements(@RequestParam(required=false) StockMovementType movementType,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate,@RequestParam(required=false) String eventName,@RequestParam(required=false) Long exitTypeId){return csv("movimentacoes.csv",service.movementsCsv(movementType,startDate,endDate,eventName,exitTypeId));}
    @GetMapping(value="/event-costs.csv",produces="text/csv;charset=UTF-8") public ResponseEntity<String> eventCosts(@RequestParam(required=false) String eventName,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate){return csv("custos-por-evento.csv",service.eventCostsCsv(eventName,startDate,endDate));}
    @GetMapping(value="/financial-summary.csv",produces="text/csv;charset=UTF-8") public ResponseEntity<String> financialSummary(){return csv("resumo-financeiro.csv",service.financialSummaryCsv());}
    private ResponseEntity<String> csv(String filename,String body){return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+filename+"\"").contentType(MediaType.parseMediaType("text/csv;charset=UTF-8")).body(body);}
}
