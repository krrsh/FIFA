package ai.jackals.fifa.orders.creation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import ai.jackals.fifa.orders.creation.entity.Orders;

/**
 * Wrapper response for list of orders GraphQL operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersResponse {
    private boolean success;
    private String message;
    private ai.jackals.fifa.orders.creation.entity.Orders order;          // single order, null if list
    private java.util.List<ai.jackals.fifa.orders.creation.entity.Orders> orders;   // list of orders, null if single
}
