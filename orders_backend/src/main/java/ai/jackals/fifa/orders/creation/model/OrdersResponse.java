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
	public OrdersResponse(boolean b, String string, Object object, Object object2) {
		// TODO Auto-generated constructor stub
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ai.jackals.fifa.orders.creation.entity.Orders getOrder() {
		return order;
	}
	public void setOrder(ai.jackals.fifa.orders.creation.entity.Orders order) {
		this.order = order;
	}
	public java.util.List<ai.jackals.fifa.orders.creation.entity.Orders> getOrders() {
		return orders;
	}
	public void setOrders(java.util.List<ai.jackals.fifa.orders.creation.entity.Orders> orders) {
		this.orders = orders;
	}
    
    
}
