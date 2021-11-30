package dao.impl;

import dao.CrudUtil;
import dao.custom.LoginDAO;
import entity.Login;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.FactoryConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginDAOImpl implements LoginDAO {
    @Override
    public boolean add(Login login) throws SQLException, ClassNotFoundException {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.save(login);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Login login) throws SQLException, ClassNotFoundException {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.update(login);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.delete(s);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public Login search(String s) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public ArrayList<Login> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not Supported Yet");
    }

    @Override
    public Login userSearch(String userName, String Password) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.executeQuery("SELECT * FROM Login WHERE UserName=? AND PassWord=?",userName,Password);
        rst.next();
        return new Login(userName,Password);
    }
}
