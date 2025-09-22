package ai.jackals.fifa.orders.creation.service;

import ai.jackals.fifa.orders.creation.entity.Orders;
import ai.jackals.fifa.orders.creation.entity.OrderItem;
import ai.jackals.fifa.orders.creation.dto.OrderReadDto;
import ai.jackals.fifa.orders.creation.dto.OrderWriteDto;
import ai.jackals.fifa.orders.creation.repository.OrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;


    @Autowired
    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    /**
     * Find all orders
     * @return List of all orders
     */
    public List<Orders> findAllOrders() {
        return ordersRepository.findByIsActiveTrue(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "orderId"));
    }

    /**
     * Find order by ID
     * @param id Order ID
     * @return Order if found
     * @throws EntityNotFoundException if order not found
     */
    public Orders findOrderById(Integer id) {
        return ordersRepository.findByOrderIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    /**
     * Save a new order
     * @param order Order to save
     * @param username Username of the creator
     * @return Saved order
     */
    @Transactional
    public Orders saveOrder(Orders order, String username) {
        order.setCreatedBy(username);
        order.setCreatedOn(LocalDateTime.now());

        // Temporarily detach items so we can get orderId first
        java.util.List<ai.jackals.fifa.orders.creation.entity.OrderItem> items = new java.util.ArrayList<>(order.getOrderItems());
        order.getOrderItems().clear();
        Orders saved = ordersRepository.saveAndFlush(order);

        // assign sequential order_item_id per order and ensure composite key contains orderId
        int seq = 1;
        for (ai.jackals.fifa.orders.creation.entity.OrderItem item : items) {
            // ensure id exists and contains orderId and orderItemId
            if (item.getId() == null) {
                item.setOrderItemId(seq++);
            } else {
                item.setOrderItemId(seq++);
            }
            item.setOrder(saved);
            // explicitly set embedded id.orderId to be safe
            if (item.getId() == null) {
                ai.jackals.fifa.orders.creation.entity.OrderItemId id = new ai.jackals.fifa.orders.creation.entity.OrderItemId();
                id.setOrderId(saved.getOrderId());
                id.setOrderItemId(item.getOrderItemId());
                item.setId(id);
            } else {
                item.getId().setOrderId(saved.getOrderId());
            }
            saved.getOrderItems().add(item);
        }

        // Persist items and return the fully flushed order
        Orders finalSaved = ordersRepository.saveAndFlush(saved);
        return finalSaved;
    }

    /**
     * Update an existing order
     * @param id Order ID
     * @param orderDetails Updated order details
     * @param username Username of the updater
     * @return Updated order
     * @throws EntityNotFoundException if order not found
     */
    @Transactional
    public Orders updateOrder(Integer id, Orders orderDetails, String username) {
        Orders order = findOrderById(id);
        
        // ---- header fields ----
    if (orderDetails.getDcNumber() != null) order.setDcNumber(orderDetails.getDcNumber());
    if (orderDetails.getPoNumber() != null) order.setPoNumber(orderDetails.getPoNumber());
    if (orderDetails.getIsUrgent() != null) order.setIsUrgent(orderDetails.getIsUrgent());
    if (orderDetails.getIsForeign() != null) order.setIsForeign(orderDetails.getIsForeign());
    if (orderDetails.getDueDate() != null) order.setDueDate(orderDetails.getDueDate());

    // ---- replace line items ----
    if (orderDetails.getOrderItems() != null) {
        // Clear existing items
        order.getOrderItems().clear();

        // assign sequential ids starting from 1
        int seq = 1;
        for (var item : orderDetails.getOrderItems()) {
            item.setOrder(order);
            item.setOrderItemId(seq++);
            order.getOrderItems().add(item);
        }
    }

    // Update remaining legacy fields if provided
        if (orderDetails.getOrderType() != null) order.setOrderType(orderDetails.getOrderType());
        if (orderDetails.getOrderService() != null) order.setOrderService(orderDetails.getOrderService());
        order.setOrderDate(orderDetails.getOrderDate());
        order.setCompanyId(orderDetails.getCompanyId());
        order.setPersonId(orderDetails.getPersonId());
        order.setSupplierId(orderDetails.getSupplierId());
        order.setCustomerId(orderDetails.getCustomerId());
        order.setMaterialId(orderDetails.getMaterialId());
        // removed productName setter
        // removed productSize setter
        // removed productSize setter
        // removed productColour setter
        // removed productStyle setter
        order.setDcNumber(orderDetails.getDcNumber());
        // removed threadType setter
        order.setIsUrgent(orderDetails.getIsUrgent());
        order.setIsForeign(orderDetails.getIsForeign());
        order.setIsAccessories(orderDetails.getIsAccessories());
        order.setDueDate(orderDetails.getDueDate());
        order.setDeliveryDate(orderDetails.getDeliveryDate());
        // removed orderQty
        // removed deliverQty
        // removed completeQty
        // removed qcQty
        // removed billQty
        // removed qtyUom
        order.setPaymentTerms(orderDetails.getPaymentTerms());
        // removed unitRate
        // removed currency
        
        // Update audit fields
        order.setChangedBy(username);
        order.setChangedOn(LocalDateTime.now());
        
        return ordersRepository.save(order);
    }

    /**
     * Delete an order by ID
     * @param id Order ID
     * @throws EntityNotFoundException if order not found
     */
    @Transactional
    public void deleteOrder(Integer id) {
        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        order.setIsActive(false);
        order.setDisabledAt(LocalDateTime.now());
        // Disable all related order items
        if (order.getOrderItems() != null) {
            LocalDateTime now = LocalDateTime.now();
            for (OrderItem item : order.getOrderItems()) {
                item.setIsActive(false);
                item.setDisabledAt(now);
            }
        }
        ordersRepository.save(order);
    }

    /**
     * Find orders by order type
     * @param orderType Order type
     * @return List of orders matching the type
     */
    public List<Orders> findOrdersByType(String orderType) {
        return ordersRepository.findByOrderType(orderType);
    }

    /**
     * Find orders by company ID
     * @param companyId Company ID
     * @return List of orders for the company
     */
    public List<Orders> findOrdersByCompany(Long companyId) {
        return ordersRepository.findByCompanyId(companyId);
    }

    /**
     * Find orders by supplier ID
     * @param supplierId Supplier ID
     * @return List of orders for the supplier
     */
    public List<Orders> findOrdersBySupplier(Long supplierId) {
        return ordersRepository.findBySupplierId(supplierId);
    }

    /**
     * Find orders by customer ID
     * @param customerId Customer ID
     * @return List of orders for the customer
     */
    public List<Orders> findOrdersByCustomer(Long customerId) {
        return ordersRepository.findByCustomerId(customerId);
    }

    /**
     * Find urgent orders
     * @return List of urgent orders
     */
    public List<Orders> findUrgentOrders() {
        return ordersRepository.findByIsUrgentTrue();
    }

    /**
     * Find orders by due date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of orders due within the date range
     */
    public List<Orders> findOrdersByDueDateRange(LocalDate startDate, LocalDate endDate) {
        return ordersRepository.findByDueDateBetween(startDate, endDate);
    }


}
