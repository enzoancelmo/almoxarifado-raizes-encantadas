package com.estoqueinteligente.stockmovement;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public class StockMovementRequest{
 @NotNull private Long productId;@NotNull private StockMovementType movementType;@NotNull @Min(0) private Integer quantity;
 @DecimalMin(value="0.0",message="Valor unitÃ¡rio nÃ£o pode ser negativo") private BigDecimal unitValue; private Long exitTypeId;
 @Size(max=255) private String reason;@Size(max=150) private String responsibleName;@Size(max=255) private String purpose;@Size(max=180) private String eventName;@Size(max=2000) private String notes;
 public Long getProductId(){return productId;}public void setProductId(Long v){productId=v;}public StockMovementType getMovementType(){return movementType;}public void setMovementType(StockMovementType v){movementType=v;}public Integer getQuantity(){return quantity;}public void setQuantity(Integer v){quantity=v;}public String getReason(){return reason;}public void setReason(String v){reason=v;}public String getResponsibleName(){return responsibleName;}public void setResponsibleName(String v){responsibleName=v;}public String getPurpose(){return purpose;}public void setPurpose(String v){purpose=v;}public String getEventName(){return eventName;}public void setEventName(String v){eventName=v;}public String getNotes(){return notes;}public void setNotes(String v){notes=v;}
 public BigDecimal getUnitValue(){return unitValue;}public void setUnitValue(BigDecimal v){unitValue=v;}public Long getExitTypeId(){return exitTypeId;}public void setExitTypeId(Long v){exitTypeId=v;}
}
