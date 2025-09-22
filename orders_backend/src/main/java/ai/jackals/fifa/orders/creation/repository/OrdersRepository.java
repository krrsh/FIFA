package ai.jackals.fifa.orders.creation.repository;

import ai.jackals.fifa.orders.creation.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    
    // Find orders by order type
    List<Orders> findByOrderType(String orderType);
    
    // Find orders by company ID
    List<Orders> findByCompanyId(Long companyId);
    
    // Find orders by supplier ID
    List<Orders> findBySupplierId(Long supplierId);
    
    // Find orders by customer ID
    List<Orders> findByCustomerId(Long customerId);
    
    // Find urgent orders
    List<Orders> findByIsUrgentTrue();
    
    // Find orders by due date
    List<Orders> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find only active orders (soft delete support)
    List<Orders> findByIsActiveTrue(Sort sort);

    // Find active order by id
    Optional<Orders> findByOrderIdAndIsActiveTrue(Integer orderId);
    

}
