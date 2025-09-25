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
    @Column(name = "product_name")
    private String productName;
    
    @Column(name = "product_style")
    private String productStyle;
    
    @Column(name = "product_colour")
    private String productColour;
    
    @Column(name = "product_size")
    private String productSize;

    @Column(name = "product_size_cm")
    private BigDecimal productSizeCm;

    @Column(name = "thread_type")
    private String threadType;

    @Column(name = "order_qty")
    private Integer orderQty;

    @Column(name = "deliver_qty")
    private Integer deliverQty;

    @Column(name = "complete_qty")
    private Integer completeQty;

    @Column(name = "qc_qty")
    private Integer qcQty;

    @Column(name = "bill_qty")
    private Integer billQty;

    @Column(name = "qty_uom")
    private String qtyUom;

    @Column(name = "unit_rate")
    private BigDecimal unitRate;

    @Column(name = "is_accessories")
    private Boolean isAccessories;

    @Column(name = "currency")
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

	public OrderItemId getId() {
		return id;
	}

	public void setId(OrderItemId id) {
		this.id = id;
	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductStyle() {
		return productStyle;
	}

	public void setProductStyle(String productStyle) {
		this.productStyle = productStyle;
	}

	public String getProductColour() {
		return productColour;
	}

	public void setProductColour(String productColour) {
		this.productColour = productColour;
	}

	public String getProductSize() {
		return productSize;
	}

	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}

	public BigDecimal getProductSizeCm() {
		return productSizeCm;
	}

	public void setProductSizeCm(BigDecimal productSizeCm) {
		this.productSizeCm = productSizeCm;
	}

	public String getThreadType() {
		return threadType;
	}

	public void setThreadType(String threadType) {
		this.threadType = threadType;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getDeliverQty() {
		return deliverQty;
	}

	public void setDeliverQty(Integer deliverQty) {
		this.deliverQty = deliverQty;
	}

	public Integer getCompleteQty() {
		return completeQty;
	}

	public void setCompleteQty(Integer completeQty) {
		this.completeQty = completeQty;
	}

	public Integer getQcQty() {
		return qcQty;
	}

	public void setQcQty(Integer qcQty) {
		this.qcQty = qcQty;
	}

	public Integer getBillQty() {
		return billQty;
	}

	public void setBillQty(Integer billQty) {
		this.billQty = billQty;
	}

	public String getQtyUom() {
		return qtyUom;
	}

	public void setQtyUom(String qtyUom) {
		this.qtyUom = qtyUom;
	}

	public BigDecimal getUnitRate() {
		return unitRate;
	}

	public void setUnitRate(BigDecimal unitRate) {
		this.unitRate = unitRate;
	}

	public Boolean getIsAccessories() {
		return isAccessories;
	}

	public void setIsAccessories(Boolean isAccessories) {
		this.isAccessories = isAccessories;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getDisabledAt() {
		return disabledAt;
	}

	public void setDisabledAt(LocalDateTime disabledAt) {
		this.disabledAt = disabledAt;
	}
    
    
}