package ai.jackals.fifa.orders.creation.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Input DTO representing a single line item for mutations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemInput {

    /**
     * Sequential identifier within an order (1,2,3...)
     */
    private Integer orderItemId;

    private OrdersInput order;

    /* ----- Item fields ----- */
    private String productName;
    @NotBlank(message = "productStyle is mandatory")
    private String productStyle;
    private String productColour;
    @NotBlank(message = "productSize is mandatory")
    private String productSize;
   @Column(name = "product_size_cm")
    private BigDecimal productSizeCm;
    private String threadType;

    @NotNull(message = "orderQty is mandatory")
    private Integer orderQty;
    private Integer deliverQty;
    private Integer completeQty;
    private Integer qcQty;
    private Integer billQty;

    private String qtyUom;
    private BigDecimal unitRate;
    private Boolean isAccessories;
    private String currency;

    @Column(name = "status")
    private String status = "ACTIVE";
}
