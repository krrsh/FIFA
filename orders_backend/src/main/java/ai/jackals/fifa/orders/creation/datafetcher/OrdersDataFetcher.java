package ai.jackals.fifa.orders.creation.datafetcher;

import ai.jackals.fifa.orders.creation.entity.Orders;
import ai.jackals.fifa.orders.creation.model.OrdersResponse;
import ai.jackals.fifa.orders.creation.service.OrdersService;
import graphql.schema.DataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrdersDataFetcher {

    private final OrdersService ordersService;

    /* ------------- QUERIES ------------- */

    public DataFetcher<OrdersResponse> orders() {
        return env -> {
            java.util.List<Orders> list = ordersService.findAllOrders();
            list.forEach(this::filterActiveItems);
            return new OrdersResponse(true, null, null, list);
        };
    }


    public DataFetcher<OrdersResponse> order() {
        return env -> {
            try {
                String idStr = env.getArgument("id");
                if (idStr == null) {
                    return new OrdersResponse(false, "Order ID is required", null, null);
                }
                Integer id = Integer.parseInt(idStr);
                Orders order = ordersService.findOrderById(id);
                if (order == null) {
                    return new OrdersResponse(false, "Order not found with ID: " + id, null, null);
                }
                filterActiveItems(order);
                return new OrdersResponse(true, null, order, null);
            } catch (NumberFormatException e) {
                return new OrdersResponse(false, "Invalid order ID format. Must be a number.", null, null);
            } catch (Exception e) {
                e.printStackTrace();
                return new OrdersResponse(false, "Error fetching order: " + e.getMessage(), null, null);
            }
        };
    }


    /* ------------- MUTATIONS ------------- */

    public DataFetcher<OrdersResponse> correctOrderItem() {
        return env -> {
            try {
                Integer orderId = env.getArgument("orderId") instanceof Integer ? env.getArgument("orderId") : Integer.parseInt(env.getArgument("orderId").toString());
                Integer itemId = env.getArgument("itemId") instanceof Integer ? env.getArgument("itemId") : Integer.parseInt(env.getArgument("itemId").toString());
                Map<String, Object> correctedItemMap = env.getArgument("correctedItem");
                if (orderId == null || itemId == null || correctedItemMap == null) {
                    return new OrdersResponse(false, "orderId, itemId and correctedItem are required", null, null);
                }

                Orders order = ordersService.findOrderById(orderId);
                if (order == null) {
                    return new OrdersResponse(false, "Order not found", null, null);
                }

                // locate the item
                ai.jackals.fifa.orders.creation.entity.OrderItem existing = order.getOrderItems().stream()
                        .filter(i -> i.getItemId().equals(itemId))
                        .findFirst()
                        .orElse(null);
                if (existing == null) {
                    return new OrdersResponse(false, "Order item not found", null, null);
                }

                existing.setStatus("CANCELLED");

                // map corrected item
                ai.jackals.fifa.orders.creation.entity.OrderItem newItem = new ai.jackals.fifa.orders.creation.entity.OrderItem();
                newItem.setProductName((String) correctedItemMap.get("productName"));
                newItem.setProductStyle((String) correctedItemMap.get("productStyle"));
                newItem.setProductColour((String) correctedItemMap.get("productColour"));
                newItem.setProductSize((String) correctedItemMap.get("productSize"));
                Object sizeCmRaw = correctedItemMap.get("productSizeCm");
                if (sizeCmRaw != null) newItem.setProductSizeCm(new java.math.BigDecimal(sizeCmRaw.toString()));
                newItem.setThreadType((String) correctedItemMap.get("threadType"));
                newItem.setOrderQty((Integer) correctedItemMap.get("orderQty"));
                newItem.setDeliverQty((Integer) correctedItemMap.get("deliverQty"));
                newItem.setCompleteQty((Integer) correctedItemMap.get("completeQty"));
                newItem.setQcQty((Integer) correctedItemMap.get("qcQty"));
                newItem.setBillQty((Integer) correctedItemMap.get("billQty"));
                newItem.setQtyUom((String) correctedItemMap.get("qtyUom"));
                Object rateRaw = correctedItemMap.get("unitRate");
                if (rateRaw != null) newItem.setUnitRate(new java.math.BigDecimal(rateRaw.toString()));
                newItem.setIsAccessories((Boolean) correctedItemMap.getOrDefault("isAccessories", false));
                newItem.setCurrency((String) correctedItemMap.getOrDefault("currency", "INR"));
                newItem.setStatus("ACTIVE");
                // assign next sequential id
                int nextSeq = order.getOrderItems().stream()
                        .map(ai.jackals.fifa.orders.creation.entity.OrderItem::getOrderItemId)
                        .filter(java.util.Objects::nonNull)
                        .max(Integer::compareTo)
                        .orElse(0) + 1;
                newItem.setOrderItemId(nextSeq);
                newItem.setOrder(order);
                order.getOrderItems().add(newItem);

                Orders updated = ordersService.saveOrder(order, "graphql-user"); // save cascades
                filterActiveItems(updated);
                return new OrdersResponse(true, "Order item corrected", updated, null);
            } catch (Exception e) {
                e.printStackTrace();
                return new OrdersResponse(false, "Error correcting order item: " + e.getMessage(), null, null);
            }
        };
    }

    public DataFetcher<OrdersResponse> createOrder() {
        return env -> {
            try {
                Map<String, Object> input = env.getArgument("input");
                if (input == null) {
                    return new OrdersResponse(false, "Input is required", null, null);
                }

                // Basic validation: supplierId is required by the schema
                Object supplierRaw = input.get("supplierId");
                if (supplierRaw == null) {
                    return new OrdersResponse(false, "supplierId is required", null, null);
                }

                Orders toSave = mapToOrderEntity(input);
                Orders saved = ordersService.saveOrder(toSave, "graphql-user");
                // reload to ensure IDs and relations are fully initialized
                Orders reloaded = ordersService.findOrderById(saved.getOrderId());
                filterActiveItems(reloaded);
                return new OrdersResponse(true, "Order created", reloaded, null);
            } catch (Exception e) {
                e.printStackTrace();
                return new OrdersResponse(false, "Error creating order: " + e.getMessage(), null, null);
            }
        };
    }

    public DataFetcher<OrdersResponse> updateOrder() {
        return env -> {
            try {
                Object idArg = env.getArgument("id");
            Integer id = null;
            if (idArg instanceof Integer) {
                id = (Integer) idArg;
            } else if (idArg != null) {
                try {
                    id = Integer.parseInt(idArg.toString());
                } catch (NumberFormatException ignored) {}
            }
                Map<String, Object> input = env.getArgument("input");
                if (id == null || input == null) {
                    return new OrdersResponse(false, "Both id and input are required", null, null);
                }
                Orders orderDetails = mapToOrderEntity(input);
                Orders updated = ordersService.updateOrder(id, orderDetails, "graphql-user");
                return new OrdersResponse(true, "Order updated", updated, null);
            } catch (Exception e) {
                e.printStackTrace();
                return new OrdersResponse(false, "Error updating order: " + e.getMessage(), null, null);
            }
        };
    }

    public DataFetcher<OrdersResponse> deleteOrder() {
        return env -> {
            Object idArg = env.getArgument("id");
            Integer id = null;
            if (idArg instanceof Integer) {
                id = (Integer) idArg;
            } else if (idArg != null) {
                try {
                    id = Integer.parseInt(idArg.toString());
                } catch (NumberFormatException ignored) {}
            }
            if (id == null) {
            return new OrdersResponse(false, "id argument is required", null, null);
        }
                        ordersService.deleteOrder(id);
            return new OrdersResponse(true, "Order deleted", null, null);
        };
    }

    /* ------------- UTIL ------------- */

    private void filterActiveItems(Orders order) {
        if (order == null || order.getOrderItems() == null) return;
        order.setOrderItems(order.getOrderItems().stream()
                .filter(i -> "ACTIVE".equals(i.getStatus()))
                .toList()); // List remains compatible
    }

    private Orders mapToOrderEntity(Map<String, Object> input) {
        java.util.List<java.util.Map<String,Object>> itemsRaw = (java.util.List<java.util.Map<String,Object>>) input.get("items");
        java.util.List<ai.jackals.fifa.orders.creation.dto.OrderItemWriteDto> items = new java.util.ArrayList<>();
        if (itemsRaw != null) {
            for (java.util.Map<String,Object> m : itemsRaw) {
                java.math.BigDecimal sizeCm = m.get("productSizeCm") == null ? null : new java.math.BigDecimal(m.get("productSizeCm").toString());
                java.math.BigDecimal unitRate = m.get("unitRate") == null ? null : new java.math.BigDecimal(m.get("unitRate").toString());
                items.add(new ai.jackals.fifa.orders.creation.dto.OrderItemWriteDto(
                        (String) m.get("productName"),
                        (String) m.get("productStyle"),
                        (String) m.get("productColour"),
                        (String) m.get("productSize"),
                        sizeCm,
                        (String) m.get("threadType"),
                        (Integer) m.get("orderQty"),
                        (Integer) m.get("deliverQty"),
                        (Integer) m.get("completeQty"),
                        (Integer) m.get("qcQty"),
                        (Integer) m.get("billQty"),
                        (String) m.get("qtyUom"),
                        unitRate,
                        (Boolean) m.get("isAccessories"),
                        (String) m.get("currency")
                ));
            }
        }

        ai.jackals.fifa.orders.creation.dto.OrderWriteDto dto = new ai.jackals.fifa.orders.creation.dto.OrderWriteDto(
                (Integer) input.get("companyId"),
                (Integer) input.get("supplierId"),
                (String) input.get("dcNumber"),
                (String) input.get("poNumber"),
                (Boolean) input.get("isUrgent"),
                (Boolean) input.get("isForeign"),
                parseDate(input.get("dueDate")),
                items
        );
        return ai.jackals.fifa.orders.creation.mapper.OrderMapper.fromWriteDto(dto);
    }

    private java.time.LocalDate parseDate(Object dateStr) {
        return dateStr == null ? null : java.time.LocalDate.parse(dateStr.toString());
    }
}
