package ai.jackals.fifa.orders.creation.mapper;

import ai.jackals.fifa.orders.creation.dto.OrderReadDto;
import ai.jackals.fifa.orders.creation.dto.OrderWriteDto;
import ai.jackals.fifa.orders.creation.entity.Orders;

/**
 * Utility class for converting between Orders entity and DTOs.
 */
public final class OrderMapper {
    private OrderMapper() {}

    public static OrderReadDto toReadDto(Orders o) {
        var first = (o.getOrderItems() == null || o.getOrderItems().isEmpty()) ? null : o.getOrderItems().get(0);
        return new OrderReadDto(
                o.getOrderId(),
                o.getSupplierId(),
                first != null ? first.getProductStyle() : null,
                null,
                o.getOrderType(),
                o.getOrderService(),
                o.getCompanyId(),
                o.getPersonId(),
                o.getCustomerId(),
                o.getMaterialId(),
                first != null ? first.getProductName() : null,
                first != null ? first.getProductSize() : null,
                first != null && first.getProductSizeCm()!=null ? first.getProductSizeCm().doubleValue() : null,
                o.getIsUrgent(),
                o.getIsForeign(),
                first != null ? first.getIsAccessories() : null,
                o.getDeliveryDate(),
                first != null ? first.getOrderQty() : null,
                first != null ? first.getDeliverQty() : null,
                first != null ? first.getCompleteQty() : null,
                null, o.getDueDate(),
                o.getDcNumber()
        );
    }

    public static Orders fromWriteDto(OrderWriteDto dto) {
        Orders o = new Orders();
        o.setCompanyId(dto.companyId());
        o.setSupplierId(dto.supplierId());
        o.setDcNumber(dto.dcNumber());
        
        o.setPoNumber(dto.poNumber());
         o.setOrderDate(java.time.LocalDate.now());
        
        
        o.setIsUrgent(dto.isUrgent() != null ? dto.isUrgent() : false);
        o.setIsForeign(dto.isForeign() != null ? dto.isForeign() : false);
        
        o.setDueDate(dto.dueDate());

        // map line items if provided
        if (dto.items() != null) {
            for (var itemDto : dto.items()) {
                ai.jackals.fifa.orders.creation.entity.OrderItem item = new ai.jackals.fifa.orders.creation.entity.OrderItem();
                item.setProductName(itemDto.productName());
                item.setProductStyle(itemDto.productStyle());
                item.setProductColour(itemDto.productColour());
                item.setProductSize(itemDto.productSize());
                item.setProductSizeCm(itemDto.productSizeCm());
                item.setThreadType(itemDto.threadType());
                item.setOrderQty(itemDto.orderQty());
                item.setDeliverQty(itemDto.deliverQty());
                item.setCompleteQty(itemDto.completeQty());
                item.setQcQty(itemDto.qcQty());
                item.setBillQty(itemDto.billQty());
                item.setQtyUom(itemDto.qtyUom());
                item.setUnitRate(itemDto.unitRate());
                item.setIsAccessories(itemDto.isAccessories() != null ? itemDto.isAccessories() : false);
                item.setCurrency(itemDto.currency() != null ? itemDto.currency() : "INR");
                item.setStatus("ACTIVE");

                item.setOrder(o); // set FK
                o.getOrderItems().add(item);
            }
        }

        return o;
    }
}
