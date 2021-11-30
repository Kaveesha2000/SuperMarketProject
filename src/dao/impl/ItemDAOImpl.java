package dao.impl;

import dao.CrudUtil;
import dao.custom.ItemDAO;
import entity.Customer;
import entity.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.FactoryConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean add(Item item) throws SQLException, ClassNotFoundException {
        /*return CrudUtil.executeUpdate("INSERT INTO Item (ItemCode, Description, PackSize, UnitPrice, QtyOnHand) VALUES (?,?,?,?,?)", item.getItemCode()
                , item.getDescription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand());*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.save(item);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        /*return CrudUtil.executeUpdate("DELETE FROM Item WHERE ItemCode=?", s);*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(s);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Item item) throws SQLException, ClassNotFoundException {
        /*return CrudUtil.executeUpdate("UPDATE Item SET Description=?, PackSize=?, UnitPrice=?, QtyOnHand=? WHERE ItemCode=?", item.getDescription(),
                item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand(),item.getItemCode());*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.update(item);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public Item search(String s) throws SQLException, ClassNotFoundException {
        /*ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Item WHERE ItemCode=?", s);
        rst.next();
        return new Item(s,
                rst.getString("Description"),
                rst.getString("PackSize"),
                rst.getDouble("UnitPrice"),
                rst.getInt("QtyOnHand")
        );*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Item item = session.get(Item.class, s);
        transaction.commit();
        session.close();
        return item;
    }

    @Override
    public ArrayList<Item> getAll() throws SQLException, ClassNotFoundException {
        /*ArrayList<Item> allItems = new ArrayList<>();
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Item");
        while (rst.next()) {
            allItems.add(new Item(
                    rst.getString("ItemCode"),
                    rst.getString("Description"),
                    rst.getString("PackSize"),
                    rst.getDouble("UnitPrice"),
                    rst.getInt("QtyOnHand"))
            );
        }
        return allItems;*/
        ArrayList<Item> items = new ArrayList();
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("FROM Item ");
        items = (ArrayList<Item>) query.list();
        transaction.commit();
        session.close();
        return items;
    }

    @Override
    public boolean ifItemExist(String code) throws SQLException, ClassNotFoundException {
        /*return CrudUtil.executeQuery("SELECT ItemCode FROM Item WHERE ItemCode=?", code).next();*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("SELECT itemCode FROM Item WHERE itemCode=:code");
        String id1 = (String) query.setParameter("code", code).uniqueResult();
        if (id1!=null){
            return true;
        }
        transaction.commit();
        session.close();
        return false;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        /*ResultSet rst = CrudUtil.executeQuery("SELECT ItemCode FROM Item ORDER BY ItemCode DESC LIMIT 1;");
        if (rst.next()) {
            String id = rst.getString("ItemCode");
            int newItemId = Integer.parseInt(id.replace("I", "")) + 1;
            return String.format("I%03d", newItemId);
        } else {
            return "I001";
        }*/
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createSQLQuery("SELECT itemCode FROM Item ORDER BY itemCode DESC LIMIT 1");
        String s = (String) query.uniqueResult();
        transaction.commit();
        session.close();
        if (s!=null) {
            int newItemId = Integer.parseInt(s.replace("I", "")) + 1;
            return String.format("I%03d", newItemId);
        }
        return "I001";
    }
}
