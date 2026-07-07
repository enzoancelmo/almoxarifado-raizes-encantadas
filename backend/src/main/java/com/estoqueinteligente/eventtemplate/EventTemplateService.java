package com.estoqueinteligente.eventtemplate;

import com.estoqueinteligente.common.BusinessException;
import com.estoqueinteligente.common.ResourceNotFoundException;
import com.estoqueinteligente.product.Product;
import com.estoqueinteligente.product.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventTemplateService {
    private final EventTemplateRepository repository; private final ProductRepository productRepository;
    public EventTemplateService(EventTemplateRepository repository,ProductRepository productRepository){this.repository=repository;this.productRepository=productRepository;}
    @Transactional(readOnly=true) public List<EventTemplateResponse> findAll(){return repository.findAllWithItems().stream().map(EventTemplateResponse::new).toList();}
    @Transactional(readOnly=true) public EventTemplateResponse findById(Long id){return new EventTemplateResponse(getEntityWithItems(id));}
    @Transactional(readOnly=true) public EventTemplate getEntity(Long id){return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Modelo de evento não encontrado"));}
    @Transactional(readOnly=true) public EventTemplate getEntityWithItems(Long id){return repository.findByIdWithItems(id).orElseThrow(()->new ResourceNotFoundException("Modelo de evento não encontrado"));}
    @Transactional public EventTemplateResponse create(EventTemplateRequest request){EventTemplate t=new EventTemplate();apply(t,request,true);return new EventTemplateResponse(repository.save(t));}
    @Transactional public EventTemplateResponse update(Long id,EventTemplateRequest request){EventTemplate t=getEntityWithItems(id);apply(t,request,false);return new EventTemplateResponse(repository.save(t));}
    @Transactional public void delete(Long id){EventTemplate t=getEntity(id);t.setActive(false);repository.save(t);}
    @Transactional public EventTemplateResponse addItem(Long id,EventTemplateItemRequest request){EventTemplate t=getEntityWithItems(id);Product p=findProduct(request.getItemId());String itemName=resolveItemName(request,p);EventTemplateItem item=findExisting(t,p,itemName);if(item==null){item=new EventTemplateItem();item.setEventTemplate(t);t.getItems().add(item);}applyItem(item,request,p,itemName);return new EventTemplateResponse(repository.save(t));}
    @Transactional public EventTemplateResponse updateItem(Long id,Long itemId,EventTemplateItemRequest request){EventTemplate t=getEntityWithItems(id);EventTemplateItem item=t.getItems().stream().filter(i->i.getId().equals(itemId)).findFirst().orElseThrow(()->new ResourceNotFoundException("Item do modelo não encontrado"));Product p=findProduct(request.getItemId());String itemName=resolveItemName(request,p);EventTemplateItem duplicated=findExisting(t,p,itemName);if(duplicated!=null&&!duplicated.getId().equals(item.getId()))throw new BusinessException("Este item já existe no modelo.");applyItem(item,request,p,itemName);return new EventTemplateResponse(repository.save(t));}
    @Transactional public EventTemplateResponse deleteItem(Long id,Long itemId){EventTemplate t=getEntityWithItems(id);boolean removed=t.getItems().removeIf(i->i.getId().equals(itemId));if(!removed)throw new ResourceNotFoundException("Item do modelo não encontrado");return new EventTemplateResponse(repository.save(t));}
    private Product findProduct(Long id){return id==null?null:productRepository.findByIdWithCategory(id).orElseThrow(()->new ResourceNotFoundException("Item não encontrado"));}
    private String resolveItemName(EventTemplateItemRequest r,Product p){String name=p==null?r.getItemName():p.getName();if(name==null||name.isBlank())throw new BusinessException("Informe um item cadastrado ou o nome do item não cadastrado.");return name.trim();}
    private EventTemplateItem findExisting(EventTemplate t,Product p,String name){return t.getItems().stream().filter(i->p!=null?i.getProduct()!=null&&i.getProduct().getId().equals(p.getId()):i.getProduct()==null&&i.getItemName().equalsIgnoreCase(name)).findFirst().orElse(null);}
    private void apply(EventTemplate t,EventTemplateRequest r,boolean creating){t.setName(r.getName().trim());t.setDescription(normalize(r.getDescription()));t.setEventType(normalize(r.getEventType()));t.setNotes(normalize(r.getNotes()));t.setActive(r.getActive()==null?creating:r.getActive());}
    private void applyItem(EventTemplateItem item,EventTemplateItemRequest r,Product p,String itemName){if(r.getSuggestedQuantity()==null||r.getSuggestedQuantity()<=0)throw new BusinessException("Quantidade sugerida deve ser maior que zero.");item.setProduct(p);item.setItemName(itemName);item.setUnitOfMeasure(p==null?normalize(r.getUnitOfMeasure()):p.getUnitOfMeasure());item.setSuggestedQuantity(r.getSuggestedQuantity());item.setNotes(normalize(r.getNotes()));}
    private String normalize(String value){return value==null||value.isBlank()?null:value.trim();}
}