package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Item {
    @Id
    private String itemCode;
    private String description;
    private String packSize;
    private double unitPrice;
    private int qtyOnHand;

    @OneToMany(mappedBy = "item")
    List<OrderDetails> ordersList=new ArrayList<>();

    public Item() {
    }

    public Item(String itemCode, String description, String packSize, double unitPrice, int qtyOnHand) {
        this.setItemCode(itemCode);
        this.setDescription(description);
        this.setPackSize(packSize);
        this.setUnitPrice(unitPrice);
        this.setQtyOnHand(qtyOnHand);
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }
}
