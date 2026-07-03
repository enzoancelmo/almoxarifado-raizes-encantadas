package com.estoqueinteligente.alert;
import java.util.List;
import org.springframework.stereotype.Service;
import com.estoqueinteligente.product.*;
@Service
public class AlertService { private final ProductService products; public AlertService(ProductService products){this.products=products;} public List<ProductResponse> lowStock(){return products.findLowStock();} public List<ProductResponse> expiring(){return products.findByCurrentStatus(ProductStatus.VENCENDO);} public List<ProductResponse> expired(){return products.findByCurrentStatus(ProductStatus.VENCIDO);} public AlertSummaryResponse summary(){return new AlertSummaryResponse(lowStock().size(),expiring().size(),expired().size());} }
