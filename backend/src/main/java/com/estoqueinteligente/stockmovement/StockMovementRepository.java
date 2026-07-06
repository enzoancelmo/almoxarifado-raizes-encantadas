package com.estoqueinteligente.stockmovement;

import java.time.Instant;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface StockMovementRepository extends JpaRepository<StockMovement,Long> {
    @Query("SELECT m FROM StockMovement m JOIN FETCH m.product ORDER BY m.createdAt DESC") List<StockMovement> findAllWithProduct();
    @Query("SELECT m FROM StockMovement m JOIN FETCH m.product LEFT JOIN FETCH m.exitType ORDER BY m.createdAt DESC") List<StockMovement> findAllWithProductAndExitType();
    @Query("SELECT m FROM StockMovement m JOIN FETCH m.product WHERE m.id=:id") Optional<StockMovement> findByIdWithProduct(@Param("id")Long id);
    @Query("SELECT m FROM StockMovement m JOIN FETCH m.product WHERE m.product.id=:productId ORDER BY m.createdAt DESC") List<StockMovement> findByProductId(@Param("productId")Long productId);
    @EntityGraph(attributePaths="product") List<StockMovement> findTop5ByOrderByCreatedAtDesc();
    @Query("SELECT COALESCE(SUM(m.quantity),0) FROM StockMovement m WHERE m.movementType=:type AND m.createdAt>=:start AND m.createdAt<:end")
    Long sumQuantityByTypeAndPeriod(@Param("type")StockMovementType type,@Param("start")Instant start,@Param("end")Instant end);
}
