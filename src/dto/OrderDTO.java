package dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class OrderDTO {
    private String orderId;
    private LocalDate orderDate;
    private LocalTime orderTime;
    private String custId;
    private String custName;
    private double cost;
    private double orderTotal;
    private List<OrderDetailDTO> orderDetail;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, LocalDate orderDate, LocalTime orderTime, String customerId, List<OrderDetailDTO> orderDetails, double orderTotal) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
        this.setCustId(customerId);
        this.setOrderDetail(orderDetails);
        this.setOrderTotal(orderTotal);
    }

    public OrderDTO(String orderId, LocalDate orderDate, LocalTime orderTime, String custId, String custName, double cost, double orderTotal, List<OrderDetailDTO> orderDetail) {
        this.setOrderId(orderId);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
        this.setCustId(custId);
        this.setCustName(custName);
        this.setCost(cost);
        this.setOrderTotal(orderTotal);
        this.setOrderDetail(orderDetail);
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

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public List<OrderDetailDTO> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetailDTO> orderDetail) {
        this.orderDetail = orderDetail;
    }
}
