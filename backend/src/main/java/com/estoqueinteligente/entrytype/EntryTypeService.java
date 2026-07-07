package com.estoqueinteligente.entrytype;

import com.estoqueinteligente.common.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntryTypeService {
    private final EntryTypeRepository repository;
    public EntryTypeService(EntryTypeRepository repository){this.repository=repository;}
    @Transactional(readOnly=true) public List<EntryTypeResponse> findAll(){return repository.findAllByOrderByNameAsc().stream().map(EntryTypeResponse::from).toList();}
    @Transactional(readOnly=true) public List<EntryTypeResponse> findActive(){return repository.findByActiveTrueOrderByNameAsc().stream().map(EntryTypeResponse::from).toList();}
    @Transactional(readOnly=true) public EntryTypeResponse findById(Long id){return EntryTypeResponse.from(getEntity(id));}
    @Transactional public EntryTypeResponse create(EntryTypeRequest r){EntryType e=new EntryType();apply(e,r,true);return EntryTypeResponse.from(repository.save(e));}
    @Transactional public EntryTypeResponse update(Long id,EntryTypeRequest r){EntryType e=getEntity(id);apply(e,r,false);return EntryTypeResponse.from(repository.save(e));}
    @Transactional public void delete(Long id){EntryType e=getEntity(id);e.setActive(false);repository.save(e);}
    @Transactional(readOnly=true) public EntryType getEntity(Long id){return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Tipo de entrada não encontrado"));}
    private void apply(EntryType e,EntryTypeRequest r,boolean creating){e.setName(r.getName().trim());e.setDescription(normalize(r.getDescription()));e.setActive(r.getActive()==null?creating:r.getActive());}
    private String normalize(String v){return v==null||v.isBlank()?null:v.trim();}
}
