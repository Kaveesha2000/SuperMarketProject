package bo.impl;

import bo.custom.PurchaseOrderBO;
import dao.DAOFactory;
import dao.custom.*;
import db.DbConnection;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import entity.Customer;
import entity.Item;
import entity.OrderDetails;
import entity.Orders;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.FactoryConfiguration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PurchaseOrderBOImpl implements PurchaseOrderBO {

    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailDAO orderDetailsDAO = (OrderDetailDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.QUERYDAO);

    @Override
    public boolean purchaseOrder(OrderDTO dto) throws SQLException, ClassNotFoundException {
        /*Transaction*//*
        Connection connection = null;

        connection = DbConnection.getInstance().getConnection();
        boolean orderAvailable = orderDAO.ifOrderExist(dto.getOrderId());
        *//*if order id already exist*//*
        if (orderAvailable) {
            return false;
        }

        connection.setAutoCommit(false);
        *//*Add Order*//*
        Orders order = new Orders(dto.getOrderId(), dto.getOrderDate(), dto.getOrderTime(), dto.getCustId(),
                dto.getOrderTotal());
        boolean orderAdded = orderDAO.add(order);
        if (!orderAdded) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }

        for (OrderDetailDTO detail : dto.getOrderDetail()) {
            OrderDetails orderDetailDTO = new OrderDetails(dto.getOrderId(),detail.getItemCode(), detail.getOrderQty(), detail.getDiscount());
            boolean orderDetailsAdded = orderDetailsDAO.add(orderDetailDTO);
            if (!orderDetailsAdded) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            //Search & Update Item
            Item search = itemDAO.search(detail.getItemCode());
            search.setQtyOnHand(search.getQtyOnHand() - detail.getOrderQty());
            boolean update = itemDAO.update(search);
            if (!update) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }

        //if every thing ok
        connection.commit();
        connection.setAutoCommit(true);
        return true;*/

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Customer customer = session.get(Customer.class, dto.getCustId());

        Orders order = new Orders(dto.getOrderId(), dto.getOrderDate(), dto.getOrderTime(),
                dto.getOrderTotal(),customer);
        OrderDetails orderDetailDTO=null;
        Item item=null;

        for (OrderDetailDTO detail : dto.getOrderDetail()) {
            item = session.get(Item.class, detail.getItemCode());
            orderDetailDTO = new OrderDetails(order,item,detail.getOrderQty(),detail.getDiscount());
            int remainingQty = item.getQtyOnHand() - detail.getOrderQty();
            item.setQtyOnHand(remainingQty);
        }
        session.save(order);
        session.save(orderDetailDTO);
        session.update(item);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewOrderId();
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        ArrayList<Customer> all = customerDAO.getAll();
        for (Customer c : all) {
            allCustomers.add(new CustomerDTO(c.getCustomerId(), c.getCustomerTitle(), c.getCustomerName()
            , c.getCustomerAddress(), c.getCustomerCity(), c.getCustomerProvince(), c.getCustomerPostalCode()));
        }
        return allCustomers;
    }

    @Override
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO> allItems = new ArrayList<>();
        ArrayList<Item> all = itemDAO.getAll();
        for (Item item : all) {
            allItems.add(new ItemDTO(item.getItemCode(), item.getDescription(),
                    item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand()));
        }
        return allItems;
    }

    @Override
    public ItemDTO searchItem(String code) throws SQLException, ClassNotFoundException {
        Item item = itemDAO.search(code);
        return new ItemDTO(item.getItemCode(), item.getDescription(),
                item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand());
    }

    @Override
    public boolean ifItemExist(String code) throws SQLException, ClassNotFoundException {
        return itemDAO.ifItemExist(code);
    }

    @Override
    public boolean ifCustomerExist(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.ifCustomerExist(id);
    }

    @Override
    public CustomerDTO searchCustomer(String s) throws SQLException, ClassNotFoundException {
        Customer c = customerDAO.search(s);
        return new CustomerDTO(c.getCustomerId(), c.getCustomerTitle(), c.getCustomerName()
                , c.getCustomerAddress(), c.getCustomerCity(), c.getCustomerProvince(), c.getCustomerPostalCode());
    }
}
