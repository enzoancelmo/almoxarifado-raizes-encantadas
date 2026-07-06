package com.estoqueinteligente.product;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
class ProductServiceTest{
 private final ProductService service=new ProductService(null,null,null);
 @Test void pendingCountHasPriority(){Product p=item(0,5,true);assertEquals(ProductStatus.PENDENTE_CONTAGEM,service.calculateStatus(p));}
 @Test void zeroWithNeedRequiresRestock(){assertEquals(ProductStatus.NECESSIDADE_REPOSICAO,service.calculateStatus(item(0,5,false)));}
 @Test void belowMonthlyNeedIsNegative(){assertEquals(ProductStatus.SALDO_NEGATIVO,service.calculateStatus(item(3,5,false)));}
 @Test void enoughQuantityIsNormal(){assertEquals(ProductStatus.NORMAL,service.calculateStatus(item(5,5,false)));}
 private Product item(int current,int monthly,boolean pending){Product p=new Product();p.setQuantity(current);p.setMonthlyRequiredQuantity(monthly);p.setCountPending(pending);return p;}
}
