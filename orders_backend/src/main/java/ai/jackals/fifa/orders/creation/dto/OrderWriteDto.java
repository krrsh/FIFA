package ai.jackals.fifa.orders.creation.dto;

import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * DTO representing payload accepted when creating an Order.
 * Contains only writable fields specified by the client.
 */
public record OrderWriteDto(
        Integer companyId,
        Integer supplierId,
        String dcNumber,
        String poNumber,
        Boolean isUrgent,
        Boolean isForeign,
        LocalDate dueDate,
        java.util.List<OrderItemWriteDto> items
) {
}
