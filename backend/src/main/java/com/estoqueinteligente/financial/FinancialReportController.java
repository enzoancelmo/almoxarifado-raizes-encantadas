package com.estoqueinteligente.financial;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/financial-reports")
public class FinancialReportController {
    private final FinancialReportService service;
    public FinancialReportController(FinancialReportService service){this.service=service;}
    @GetMapping("/summary") public FinancialSummaryResponse summary(){return service.summary();}
    @GetMapping("/event-costs") public EventCostResponse eventCosts(@RequestParam(required=false) String eventName,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate,@RequestParam(required=false) Long exitTypeId){return service.eventCosts(eventName,startDate,endDate,exitTypeId);}
}
