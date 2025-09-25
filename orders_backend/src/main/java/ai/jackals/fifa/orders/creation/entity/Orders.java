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
import java.util.Objects;
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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderService() {
		return orderService;
	}

	public void setOrderService(String orderService) {
		this.orderService = orderService;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	public Boolean getIsUrgent() {
		return isUrgent;
	}

	public void setIsUrgent(Boolean isUrgent) {
		this.isUrgent = isUrgent;
	}

	public Boolean getIsForeign() {
		return isForeign;
	}

	public void setIsForeign(Boolean isForeign) {
		this.isForeign = isForeign;
	}

	public Boolean getIsAccessories() {
		return isAccessories;
	}

	public void setIsAccessories(Boolean isAccessories) {
		this.isAccessories = isAccessories;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDcNumber() {
		return dcNumber;
	}

	public void setDcNumber(String dcNumber) {
		this.dcNumber = dcNumber;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	public void setChangedOn(LocalDateTime changedOn) {
		this.changedOn = changedOn;
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

	public java.util.List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(java.util.List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@Override
	public int hashCode() {
		return Objects.hash(changedBy, changedOn, companyId, createdBy, createdOn, customerId, dcNumber, deliveryDate,
				disabledAt, dueDate, isAccessories, isActive, isForeign, isUrgent, materialId, orderDate, orderId,
				orderItems, orderService, orderType, paymentTerms, personId, poNumber, supplierId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Orders other = (Orders) obj;
		return Objects.equals(changedBy, other.changedBy) && Objects.equals(changedOn, other.changedOn)
				&& Objects.equals(companyId, other.companyId) && Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(createdOn, other.createdOn) && Objects.equals(customerId, other.customerId)
				&& Objects.equals(dcNumber, other.dcNumber) && Objects.equals(deliveryDate, other.deliveryDate)
				&& Objects.equals(disabledAt, other.disabledAt) && Objects.equals(dueDate, other.dueDate)
				&& Objects.equals(isAccessories, other.isAccessories) && Objects.equals(isActive, other.isActive)
				&& Objects.equals(isForeign, other.isForeign) && Objects.equals(isUrgent, other.isUrgent)
				&& Objects.equals(materialId, other.materialId) && Objects.equals(orderDate, other.orderDate)
				&& Objects.equals(orderId, other.orderId) && Objects.equals(orderItems, other.orderItems)
				&& Objects.equals(orderService, other.orderService) && Objects.equals(orderType, other.orderType)
				&& Objects.equals(paymentTerms, other.paymentTerms) && Objects.equals(personId, other.personId)
				&& Objects.equals(poNumber, other.poNumber) && Objects.equals(supplierId, other.supplierId);
	}

	@Override
	public String toString() {
		return "Orders [orderId=" + orderId + ", orderType=" + orderType + ", orderService=" + orderService
				+ ", orderDate=" + orderDate + ", companyId=" + companyId + ", personId=" + personId + ", supplierId="
				+ supplierId + ", customerId=" + customerId + ", materialId=" + materialId + ", isUrgent=" + isUrgent
				+ ", isForeign=" + isForeign + ", isAccessories=" + isAccessories + ", dueDate=" + dueDate
				+ ", deliveryDate=" + deliveryDate + ", dcNumber=" + dcNumber + ", poNumber=" + poNumber
				+ ", paymentTerms=" + paymentTerms + ", createdBy=" + createdBy + ", createdOn=" + createdOn
				+ ", changedBy=" + changedBy + ", changedOn=" + changedOn + ", isActive=" + isActive + ", disabledAt="
				+ disabledAt + ", orderItems=" + orderItems + "]";
	}
    
    

}
