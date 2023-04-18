
package net.affliction.karos.enums;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderItem {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private Integer description;
    @SerializedName("delivery_status")
    @Expose
    private String deliveryStatus;
    @SerializedName("price")
    @Expose
    private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDescription() {
        return description;
    }

    public void setDescription(Integer description) {
        this.description = description;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
