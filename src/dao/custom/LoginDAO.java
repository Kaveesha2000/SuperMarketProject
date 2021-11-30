package dao.custom;

import dao.CrudDAO;
import entity.Login;

import java.sql.SQLException;

public interface LoginDAO extends CrudDAO<Login,String> {
    Login userSearch(String userName,String Password) throws SQLException, ClassNotFoundException;
}
