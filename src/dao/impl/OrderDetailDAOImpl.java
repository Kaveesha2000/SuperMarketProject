package dao.impl;

import dao.CrudUtil;
import dao.custom.OrderDetailDAO;
import entity.OrderDetails;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.FactoryConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public boolean add(OrderDetails orderDetails) throws SQLException, ClassNotFoundException {
       /* return CrudUtil.executeUpdate("INSERT INTO OrderDetail VALUES (?,?,?,?)", orderDetails.getOrderId(), orderDetails.getItemCode(),
                orderDetails.getOrderQty(), orderDetails.getDiscount());*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.save(orderDetails);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public boolean update(OrderDetails orderDetails) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public OrderDetails search(String s) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public ArrayList<OrderDetails> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }
}
