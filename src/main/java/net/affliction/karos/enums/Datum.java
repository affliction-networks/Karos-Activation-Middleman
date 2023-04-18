
package net.affliction.karos.enums;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("order_code")
    @Expose
    private String orderCode;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("delivery_status")
    @Expose
    private String deliveryStatus;
    @SerializedName("shipping_type")
    @Expose
    private String shippingType;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("shipping_address")
    @Expose
    private ShippingAddress shippingAddress;
    @SerializedName("shipping_cost")
    @Expose
    private String shippingCost;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("coupon_discount")
    @Expose
    private String couponDiscount;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("order_items")
    @Expose
    private List<OrderItem> orderItems;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(String couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

}
