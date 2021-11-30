package dao.impl;

import dao.CrudUtil;
import dao.custom.OrderDAO;
import entity.Customer;
import entity.Orders;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.FactoryConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean add(Orders orders) throws SQLException, ClassNotFoundException {
        /*return CrudUtil.executeUpdate("INSERT INTO Orders VALUES (?,?,?,?,?)", orders.getOrderId(),
                orders.getOrderDate(), orders.getOrderTime(), orders.getCustId(), orders.getCost());*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.save(orders);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public boolean update(Orders orders) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public Orders search(String s) throws SQLException, ClassNotFoundException {
        /*ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Orders WHERE OrderId=?", s);
        rst.next();
        return new Orders(
                rst.getString("OrderId"),
                LocalDate.parse(rst.getString("OrderDate")),
                LocalTime.parse(rst.getString("OrderTime")),
                rst.getString("CustId"),
                rst.getDouble("Cost")
        );*/

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Query s1 = session.createQuery("FROM Orders WHERE orderId=:s").setParameter("s", s);
        List<Orders> list = s1.list();
        transaction.commit();
        session.close();
        Orders orders=null;
        for (Orders temp:list) {
            orders = new Orders(temp.getOrderId(), temp.getOrderDate(), temp.getOrderTime(), temp.getCost(), temp.getCustomer());
        }
        return orders;
    }

    @Override
    public ArrayList<Orders> getAll() throws SQLException, ClassNotFoundException {
        /*ArrayList<Orders> allOrders = new ArrayList();
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Orders");
        while (rst.next()) {
            allOrders.add(new Orders(
                    rst.getString("OrderId"),
                    LocalDate.parse(rst.getString("OrderDate")),
                    LocalTime.parse(rst.getString("OrderTime")),
                    rst.getString("CustId"),
                    rst.getDouble("Cost"))
            );
        }
        return allOrders;*/
        ArrayList<Orders> allOrders = new ArrayList();

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Query orders = session.createQuery("FROM Orders");
        allOrders = (ArrayList<Orders>) orders.list();
        transaction.commit();
        session.close();
        return allOrders;
    }

    @Override
    public boolean ifOrderExist(String oid) throws SQLException, ClassNotFoundException {
        /*ResultSet rst = CrudUtil.executeQuery("SELECT OrderId FROM Orders WHERE OrderId=?", oid);
        return rst.next();*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("SELECT orderId FROM Orders WHERE orderId=:oid");
        String id1 = (String) query.setParameter("oid", oid).uniqueResult();
        if (id1!=null){
            return true;
        }
        transaction.commit();
        session.close();
        return false;
    }

    @Override
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        /*ResultSet rst = CrudUtil.executeQuery("SELECT OrderId FROM Orders ORDER BY OrderId DESC LIMIT 1;");
        return rst.next() ? String.format("OD%03d", (Integer.parseInt(rst.getString("OrderId").replace("OD", "")) + 1)) : "OD001";
   */
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createSQLQuery("SELECT orderId FROM Orders ORDER BY orderId DESC LIMIT 1");
        String s = (String) query.uniqueResult();
        transaction.commit();
        session.close();
        if (s!=null) {
            int newOrderId = Integer.parseInt(s.replace("O", "")) + 1;
            return String.format("O%03d", newOrderId);
        }
        return "O001";
    }
}
