package ai.jackals.fifa.orders.creation.repository;

import ai.jackals.fifa.orders.creation.entity.OrderItem;
import ai.jackals.fifa.orders.creation.entity.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link OrderItem} entity.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

    /**
     * Find all items belonging to an order.
     *
     * @param orderId parent order id
     * @return list of items
     */
    List<OrderItem> findByIdOrderId(Integer orderId);

    /**
     * Get the max orderItemId for a specific order.
     */
    Integer countByIdOrderId(Integer orderId);
}
