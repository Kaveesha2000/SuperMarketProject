package dao.impl;

import dao.CrudUtil;
import dao.custom.CustomerDAO;
import entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.FactoryConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public boolean add(Customer customer) throws SQLException, ClassNotFoundException {
        /*return CrudUtil.executeUpdate("INSERT INTO Customer VALUES (?,?,?,?,?,?,?)", customer.getCustomerId(), customer.getCustomerTitle(), customer.getCustomerName(),
                customer.getCustomerAddress(), customer.getCustomerCity(), customer.getCustomerProvince(),
                customer.getCustomerPostalCode());*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.save(customer);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {

        /*return CrudUtil.executeUpdate("DELETE FROM Customer WHERE CustId=?", s);*/

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(s);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Customer customer) throws SQLException, ClassNotFoundException {
        /*return CrudUtil.executeUpdate("UPDATE Customer SET CustTitle=?,CustName=?, CustAddress=?, City=?,Province=?,PostalCode=? WHERE CustId=?", customer.getCustomerTitle(), customer.getCustomerName(),
                customer.getCustomerAddress(), customer.getCustomerCity(), customer.getCustomerProvince(),
                customer.getCustomerPostalCode(), customer.getCustomerId());*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.update(customer);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public Customer search(String s) throws SQLException, ClassNotFoundException {
        /*ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customer WHERE CustId=?", s);
        rst.next();
        return new Customer(s,
                rst.getString("CustTitle"),
                rst.getString("CustName"),
                rst.getString("CustAddress"),
                rst.getString("City"),
                rst.getString("Province"),
                rst.getString("PostalCode"));*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Customer customer = session.get(Customer.class, s);
        transaction.commit();
        session.close();
        return customer;
    }

    @Override
    public ArrayList<Customer> getAll() throws SQLException, ClassNotFoundException {
        /*ArrayList<Customer> allCustomers = new ArrayList();
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customer");
        while (rst.next()) {
            allCustomers.add(new Customer(
                    rst.getString("CustId"),
                    rst.getString("CustTitle"),
                    rst.getString("CustName"),
                    rst.getString("CustAddress"),
                    rst.getString("City"),
                    rst.getString("Province"),
                    rst.getString("PostalCode"))
            );
        }
        return allCustomers;*/
        ArrayList<Customer> allCustomers = new ArrayList();
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("FROM Customer");
        allCustomers = (ArrayList<Customer>) query.list();
        transaction.commit();
        session.close();
        return allCustomers;
    }

    @Override
    public boolean ifCustomerExist(String id) throws SQLException, ClassNotFoundException {
        /*return CrudUtil.executeQuery("SELECT CustId FROM Customer WHERE CustId=?", id).next();*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("SELECT customerId FROM Customer WHERE customerId=:id");
        String id1 = (String) query.setParameter("id", id).uniqueResult();
        if (id1!=null){
            return true;
        }
        transaction.commit();
        session.close();
        return false;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        /*ResultSet rst = CrudUtil.executeQuery("SELECT CustId FROM Customer ORDER BY CustId DESC LIMIT 1;");
        if (rst.next()) {
            String id = rst.getString("CustId");
            int newCustomerId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C%03d", newCustomerId);
        } else {
            return "C001";
        }*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createSQLQuery("SELECT customerId FROM Customer ORDER BY customerId DESC LIMIT 1");
        String s = (String) query.uniqueResult();
        transaction.commit();
        session.close();
        if (s!=null) {
            int newCustomerId = Integer.parseInt(s.replace("C", "")) + 1;
            return String.format("C%03d", newCustomerId);
        }
        return "C001";
    }
}
