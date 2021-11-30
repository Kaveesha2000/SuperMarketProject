package bo.impl;

import bo.custom.CustomerBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dto.CustomerDTO;
import entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO {

    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public ArrayList<CustomerDTO> getAllCustomer() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        ArrayList<Customer> all = customerDAO.getAll();
        for (Customer customer : all) {
            allCustomers.add(new CustomerDTO(customer.getCustomerId(), customer.getCustomerTitle(), customer.getCustomerName(),
                    customer.getCustomerAddress(), customer.getCustomerCity(), customer.getCustomerProvince(),
                    customer.getCustomerPostalCode()));
        }
        return allCustomers;
    }

    @Override
    public boolean addCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        return customerDAO.add(new Customer(customerDTO.getCustomerId(), customerDTO.getCustomerTitle(), customerDTO.getCustomerName(),
                customerDTO.getCustomerAddress(), customerDTO.getCustomerCity(), customerDTO.getCustomerProvince(),
                customerDTO.getCustomerPostalCode()));
    }

    @Override
    public boolean updateCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(customerDTO.getCustomerId(), customerDTO.getCustomerTitle(), customerDTO.getCustomerName(),
                customerDTO.getCustomerAddress(), customerDTO.getCustomerCity(), customerDTO.getCustomerProvince(),
                customerDTO.getCustomerPostalCode()));
    }

    @Override
    public boolean ifCustomerExist(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.ifCustomerExist(id);
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        return customerDAO.generateNewID();
    }
}
