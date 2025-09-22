package ai.jackals.fifa.orders.creation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Projection DTO for exposing Order data via GET endpoints.
 * Only contains fields requested by the client.
 */
public record OrderReadDto(
        Integer orderId,
        Integer supplierId,
        String productStyle,
        String orderItem,
        String orderType,
        String orderService,
        Integer companyId,
        Integer personId,
        Integer customerId,
        Integer materialId,
        String productName,
        String productSizeCm,
        Double productSizeNum,
        Boolean isUrgent,
        Boolean isForeign,
        Boolean isAccessories,
        LocalDate deliveryDate,
        Integer orderQty,
        Integer deliverQty,
        Integer completeQty,
        Integer qcQty,
        LocalDate dueDate,
        String dcNumber
) {
}
