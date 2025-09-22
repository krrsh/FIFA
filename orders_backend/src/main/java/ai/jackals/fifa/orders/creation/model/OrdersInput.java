package ai.jackals.fifa.orders.creation.model;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Order_ID")
    private Integer orderId;

    @Column(name = "Order_Type")
    private String orderType = "JB-Stiching";

    @Column(name = "Order_Service")
    private String orderService;

    @NotNull(message = "Order date is required")
    @PastOrPresent(message = "Order date cannot be in the future")
    @Column(name = "Order_Date")
    private LocalDate orderDate;

    @Column(name = "Company_ID")
    private Integer companyId;

    @Column(name = "Person_ID")
    private Integer personId;

    @NotNull(message = "Supplier is required")
    @Column(name = "Supplier_ID")
    private Integer supplierId;

    @Column(name = "Customer_ID")
    private Integer customerId;

    @Column(name = "Material_ID")
    private Integer materialId;

    /* Header-level fields only; product and qty details live in OrderItem */

    @Column(name = "Dc_Number")
    private String dcNumber;

    @Column(name = "Po_Number")
    private String poNumber;

    @Column(name = "Is_Urgent")
    private Boolean isUrgent = false;

    @Column(name = "Is_Foreign")
    private Boolean isForeign = false;

    @Column(name = "Due_Date")
    private LocalDate dueDate;

    @Column(name = "Delivery_Date")
    private LocalDate deliveryDate;

    @Column(name = "Payment_Terms")
    private String paymentTerms;

    @Column(name = "Created_On", updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "Created_By", updatable = false)
    private String createdBy;

    @Column(name = "Changed_On")
    private LocalDateTime changedOn;

    @Column(name = "Changed_By")
    private String changedBy;

    /* ---------- line items ---------- */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<OrderItemInput> orderItems = new java.util.ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        changedOn = LocalDateTime.now();
    }
}
