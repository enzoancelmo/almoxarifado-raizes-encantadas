package com.estoqueinteligente.exittype;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExitTypeRepository extends JpaRepository<ExitType,Long> {
    List<ExitType> findAllByOrderByNameAsc();
    List<ExitType> findByActiveTrueOrderByNameAsc();
}
