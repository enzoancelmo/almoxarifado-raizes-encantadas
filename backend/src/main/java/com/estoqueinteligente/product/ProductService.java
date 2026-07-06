package com.estoqueinteligente.product;

import com.estoqueinteligente.category.CategoryService;
import com.estoqueinteligente.common.ResourceNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository repository; private final CategoryService categories;
    public ProductService(ProductRepository repository,CategoryService categories,com.estoqueinteligente.supplier.SupplierService ignored){this.repository=repository;this.categories=categories;}
    @Transactional(readOnly=true) public List<ProductResponse> findAll(){return repository.findAllWithCategory().stream().map(p->ProductResponse.from(p,calculateStatus(p))).toList();}
    @Transactional(readOnly=true) public List<ProductResponse> findByCurrentStatus(ProductStatus s){return repository.findAllWithCategory().stream().filter(p->calculateStatus(p)==s).map(p->ProductResponse.from(p,s)).toList();}
    @Transactional(readOnly=true) public List<ProductResponse> findLowStock(){return repository.findAllWithCategory().stream().filter(p->calculateStatus(p)!=ProductStatus.NORMAL).map(p->ProductResponse.from(p,calculateStatus(p))).toList();}
    @Transactional(readOnly=true) public ProductResponse findById(Long id){Product p=getEntity(id);return ProductResponse.from(p,calculateStatus(p));}
    @Transactional public ProductResponse create(ProductRequest request){Product p=new Product();apply(p,request);p.setStatus(calculateStatus(p));return ProductResponse.from(repository.save(p),p.getStatus());}
    @Transactional public ProductResponse update(Long id,ProductRequest request){Product p=getEntity(id);apply(p,request);p.setStatus(calculateStatus(p));return ProductResponse.from(repository.save(p),p.getStatus());}
    @Transactional public void delete(Long id){repository.delete(getEntity(id));}
    private Product getEntity(Long id){return repository.findByIdWithCategory(id).orElseThrow(()->new ResourceNotFoundException("Item não encontrado"));}
    private void apply(Product p,ProductRequest r){
        p.setName(r.name().trim());p.setBrand(normalize(r.getBrand()));p.setEntityPurpose(normalize(r.getEntityPurpose()));
        p.setUnitOfMeasure(normalize(r.getUnitOfMeasure()));p.setCategory(categories.getEntity(r.categoryId()));
        p.setQuantity(r.getCurrentQuantity().intValue());p.setMonthlyRequiredQuantity(r.getMonthlyRequiredQuantity().intValue());
        p.setMinimumStock(p.getMonthlyRequiredQuantity());p.setCountPending(Boolean.TRUE.equals(r.getCountPending()));
        p.setPurchaseValue(defaultMoney(r.getPurchaseValue()));p.setExitValue(defaultMoney(r.getExitValue()));
        p.setNotes(normalize(r.getNotes()));p.setCostPrice(p.getPurchaseValue());p.setSalePrice(p.getExitValue());
    }
    private BigDecimal defaultMoney(BigDecimal v){return v==null?BigDecimal.ZERO:v;}
    private String normalize(String v){return v==null||v.isBlank()?null:v.trim();}
    public ProductStatus calculateStatus(Product p){
        if(p.isCountPending())return ProductStatus.PENDENTE_CONTAGEM;
        int current=p.getQuantity(),monthly=p.getMonthlyRequiredQuantity();
        if(current==0&&monthly>0)return ProductStatus.NECESSIDADE_REPOSICAO;
        if(monthly>0&&current<monthly)return ProductStatus.SALDO_NEGATIVO;
        return ProductStatus.NORMAL;
    }
}
