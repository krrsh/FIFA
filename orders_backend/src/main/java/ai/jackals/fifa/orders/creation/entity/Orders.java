package ai.jackals.fifa.orders.creation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Root JPA entity representing an Order header.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    /* ----- Legacy flat fields ----- */
    //@Transient
    //private String orderItem; // kept for backwards-compatibility

    @Column(name = "order_type")
    private String orderType = "JB-Stiching";

    @Column(name = "order_service")
    private String orderService;

    @NotNull(message = "Order date is required")
    @PastOrPresent(message = "Order date cannot be in the future")
    @Column(name = "order_date")
    private LocalDate orderDate;

    /* Header details */
    private Integer companyId;
    private Integer personId;
    private Integer supplierId;
    private Integer customerId;
    @Transient
    private Integer materialId; // moved to order_items table; not persisted at header level
    private Boolean isUrgent;
    private Boolean isForeign;
    @Transient
    private Boolean isAccessories;
    private LocalDate dueDate;
    private LocalDate deliveryDate;

    @Column(name = "dc_number")
    private String dcNumber;

    @Column(name = "po_number")
    private String poNumber;

    @Column(name = "payment_terms")
    private String paymentTerms;

    /* ----- Audit ----- */
    private String createdBy;
    private LocalDateTime createdOn;
    private String changedBy;
    private LocalDateTime changedOn;

    /* ----- Soft delete ----- */
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "disabled_at")
    private LocalDateTime disabledAt;

    /* ----- Child line items ----- */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<OrderItem> orderItems = new java.util.ArrayList<>();

}
