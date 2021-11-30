package entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Orders {
    @Id
    private String orderId;

    private LocalDate orderDate;
    private LocalTime orderTime;
    private double cost;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderDetails> itemList = new ArrayList<>();

    public Orders() {
    }

    /*public Orders(String orderId, LocalDate orderDate, LocalTime orderTime, double cost) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
        this.setCost(cost);
    }*/

    public Orders(String orderId, LocalDate orderDate, LocalTime orderTime, double cost, Customer customer) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
        this.setCost(cost);
        this.setCustomer(customer);
    }

    public Orders(String orderId, LocalDate orderDate, LocalTime orderTime, double cost, Customer customer, List<OrderDetails> itemList) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
        this.setCost(cost);
        this.setCustomer(customer);
        this.setItemList(itemList);
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalTime orderTime) {
        this.orderTime = orderTime;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderDetails> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderDetails> itemList) {
        this.itemList = itemList;
    }
}
