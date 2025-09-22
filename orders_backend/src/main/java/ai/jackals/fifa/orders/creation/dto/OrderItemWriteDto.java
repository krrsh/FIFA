package ai.jackals.fifa.orders.creation.dto;

import java.math.BigDecimal;

/**
 * DTO representing a single line item in an Order create/update request.
 */
public record OrderItemWriteDto(
        String productName,
        String productStyle,
        String productColour,
        String productSize,
        BigDecimal productSizeCm,
        String threadType,
        Integer orderQty,
        Integer deliverQty,
        Integer completeQty,
        Integer qcQty,
        Integer billQty,
        String qtyUom,
        BigDecimal unitRate,
        Boolean isAccessories,
        String currency
) {}
