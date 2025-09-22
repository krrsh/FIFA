package ai.jackals.fifa.orders.creation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA entity representing a single line item belonging to an {@link Orders}.
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    /* ----- Composite PK ----- */
    @EmbeddedId
    private OrderItemId id;

    /* ----- Parent order ----- */
    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order;

    /* ----- Item fields ----- */
    private String productName;
    private String productStyle;
    private String productColour;
    private String productSize;

    @Column(name = "product_size_cm")
    private BigDecimal productSizeCm;

    private String threadType;

    private Integer orderQty;
    private Integer deliverQty;
    private Integer completeQty;
    private Integer qcQty;
    private Integer billQty;

    private String qtyUom;
    private BigDecimal unitRate;
    private Boolean isAccessories;
    private String currency;

    @Column(name = "material_id")
    private Integer materialId;

    @Column(name = "status")
    private String status = "ACTIVE";

    /* ----- Soft delete ----- */
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "disabled_at")
    private LocalDateTime disabledAt;

    /* ----- Convenience accessors ----- */
    @Transient
    public Integer getOrderItemId() {
        return id == null ? null : id.getOrderItemId();
    }

    public void setOrderItemId(Integer orderItemId) {
        if (this.id == null) {
            this.id = new OrderItemId();
        }
        this.id.setOrderItemId(orderItemId);
    }

    /**
     * Deprecated alias for legacy code referencing itemId; retained to avoid breaking GraphQL schema.
     */
    @Deprecated
    @Transient
    public Integer getItemId() {
        return getOrderItemId();
    }

    @Deprecated
    public void setItemId(Integer itemId) {
        setOrderItemId(itemId);
    }
}
