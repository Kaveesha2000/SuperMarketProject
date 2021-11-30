package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderDetails {
    @Id
    @GeneratedValue
    private int orderDetailId;
    @ManyToOne
    private Orders order;
    @ManyToOne
    private Item item;

    private int orderQty;
    private double discount;

    public OrderDetails() {
    }

    public OrderDetails(int orderDetailId, Orders order, Item item, int orderQty, double discount) {
        this.setOrderDetailId(orderDetailId);
        this.setOrder(order);
        this.setItem(item);
        this.setOrderQty(orderQty);
        this.setDiscount(discount);
    }

    public OrderDetails(Orders order, Item item, int orderQty, double discount) {
        this.order = order;
        this.item = item;
        this.orderQty = orderQty;
        this.discount = discount;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
