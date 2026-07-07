package com.estoqueinteligente.eventtemplate;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface EventTemplateRepository extends JpaRepository<EventTemplate,Long> {
    @Query("SELECT DISTINCT t FROM EventTemplate t LEFT JOIN FETCH t.items i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.category ORDER BY t.name") List<EventTemplate> findAllWithItems();
    @Query("SELECT DISTINCT t FROM EventTemplate t LEFT JOIN FETCH t.items i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.category WHERE t.id=:id") Optional<EventTemplate> findByIdWithItems(@Param("id")Long id);
}