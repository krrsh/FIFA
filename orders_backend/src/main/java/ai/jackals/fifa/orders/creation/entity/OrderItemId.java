package ai.jackals.fifa.orders.creation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Embedded composite primary key for OrderItem: (order_id, order_item_id)
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemId implements Serializable {

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "order_item_id")
    private Integer orderItemId;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, orderItemId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderItemId other = (OrderItemId) obj;
		return Objects.equals(orderId, other.orderId) && Objects.equals(orderItemId, other.orderItemId);
	}

	@Override
	public String toString() {
		return "OrderItemId [orderId=" + orderId + ", orderItemId=" + orderItemId + "]";
	}
    
    
}
