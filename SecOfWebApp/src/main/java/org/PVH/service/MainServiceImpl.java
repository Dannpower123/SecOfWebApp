package org.PVH.service;


import org.PVH.model.Customer;
import org.PVH.model.CustomerDTO;
import org.PVH.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MainServiceImpl implements MainService {

    private CustomerRepository customerRepository;

    @Autowired
    public MainServiceImpl(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> findCustomerByCustomerID(Long customerID) {
        return customerRepository.findCustomerByCustomerID(customerID);
    }

    @Override
    public void deleteCustomerByCustomerID(Long customerID) {
        customerRepository.deleteCustomerByCustomerID(customerID);
    }

    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    @Override
    public Customer saveCustomer(CustomerDTO customer) {
        Customer c = new Customer(customer.getVorname(),customer.getNachname(), customer.getEmail());
        Customer c1 = customerRepository.save(c);
        customerRepository.flush();
        return c1;
    }
}
