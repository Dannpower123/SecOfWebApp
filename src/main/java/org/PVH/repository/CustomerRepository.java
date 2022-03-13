package org.PVH.repository;

import org.PVH.model.Customer;
import org.PVH.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByCustomerID(Long customerID);
    Optional<Customer> findCustomerByEmail(String email);
    void deleteCustomerByCustomerID(Long customerID);
}
