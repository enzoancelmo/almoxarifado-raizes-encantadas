package com.estoqueinteligente.entrytype;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryTypeRepository extends JpaRepository<EntryType,Long> {
    List<EntryType> findAllByOrderByNameAsc();
    List<EntryType> findByActiveTrueOrderByNameAsc();
}
