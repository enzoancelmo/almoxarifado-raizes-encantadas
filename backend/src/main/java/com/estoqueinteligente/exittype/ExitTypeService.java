package com.estoqueinteligente.exittype;

import com.estoqueinteligente.common.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExitTypeService {
    private final ExitTypeRepository repository;
    public ExitTypeService(ExitTypeRepository repository){this.repository=repository;}
    @Transactional(readOnly=true) public List<ExitTypeResponse> findAll(){return repository.findAllByOrderByNameAsc().stream().map(ExitTypeResponse::from).toList();}
    @Transactional(readOnly=true) public List<ExitTypeResponse> findActive(){return repository.findByActiveTrueOrderByNameAsc().stream().map(ExitTypeResponse::from).toList();}
    @Transactional(readOnly=true) public ExitTypeResponse findById(Long id){return ExitTypeResponse.from(getEntity(id));}
    @Transactional public ExitTypeResponse create(ExitTypeRequest r){ExitType e=new ExitType();apply(e,r,true);return ExitTypeResponse.from(repository.save(e));}
    @Transactional public ExitTypeResponse update(Long id,ExitTypeRequest r){ExitType e=getEntity(id);apply(e,r,false);return ExitTypeResponse.from(repository.save(e));}
    @Transactional public void delete(Long id){ExitType e=getEntity(id);e.setActive(false);repository.save(e);}
    @Transactional(readOnly=true) public ExitType getEntity(Long id){return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Tipo de saÃ­da nÃ£o encontrado"));}
    private void apply(ExitType e,ExitTypeRequest r,boolean creating){e.setName(r.getName().trim());e.setDescription(normalize(r.getDescription()));e.setActive(r.getActive()==null?creating:r.getActive());}
    private String normalize(String v){return v==null||v.isBlank()?null:v.trim();}
}
