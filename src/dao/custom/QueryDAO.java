package dao.custom;

import dao.CrudDAO;
import dao.SuperDAO;
import dto.CustomDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {
    ArrayList<CustomDTO> getOrderDetailsFromOrderID(String oid) throws SQLException, ClassNotFoundException;

}
