package com.estoqueinteligente.dashboard;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/dashboard")
public class DashboardController { private final DashboardService service; public DashboardController(DashboardService service){this.service=service;} @GetMapping("/summary") public DashboardSummaryResponse summary(){return service.getSummary();} }
