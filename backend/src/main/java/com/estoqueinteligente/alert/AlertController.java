package com.estoqueinteligente.alert;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.estoqueinteligente.product.ProductResponse;
@RestController @RequestMapping("/alerts")
public class AlertController { private final AlertService service; public AlertController(AlertService service){this.service=service;} @GetMapping("/low-stock") public List<ProductResponse> lowStock(){return service.lowStock();} @GetMapping("/expiring") public List<ProductResponse> expiring(){return service.expiring();} @GetMapping("/expired") public List<ProductResponse> expired(){return service.expired();} @GetMapping("/summary") public AlertSummaryResponse summary(){return service.summary();} }
