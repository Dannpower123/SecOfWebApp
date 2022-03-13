package org.PVH.service;

import org.PVH.model.Customer;
import org.PVH.model.CustomerDTO;

import java.util.Optional;

public interface MainService {
    Optional<Customer> findCustomerByCustomerID(Long customerID);
    void deleteCustomerByCustomerID(Long customerID);
    Optional<Customer> findCustomerByEmail(String email);
    Customer saveCustomer(CustomerDTO customer);
}
