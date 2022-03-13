package org.PVH.rest;

import org.PVH.model.Customer;
import org.PVH.model.CustomerDTO;
import org.PVH.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/customer")
public class CustomerController {

    @Autowired
    private MainService mainService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @RequestMapping(value = "/{customerID}", method = RequestMethod.GET, produces = "application/json")
    public Customer getCustomer(@PathVariable("customerID") long customerID) {
        Optional<Customer> customer = this.mainService.findCustomerByCustomerID(customerID);

        if (customer.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return customer.get();
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Customer> addCustomer(@RequestBody @Valid CustomerDTO customer, BindingResult bindingResult,
                                                UriComponentsBuilder ucBuilder) {
        Optional<Customer> customerCheck = this.mainService.findCustomerByEmail(customer.getEmail());
        if(customerCheck.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email already taken");
        }
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (customer == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<Customer>(headers, HttpStatus.BAD_REQUEST);
        }
        Customer c = this.mainService.saveCustomer(customer);
        headers.setLocation(ucBuilder.path("/api/glasses/{id}").buildAndExpand(c.getCustomerID()).toUri());

        return new ResponseEntity<Customer>(c, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @RequestMapping(value = "/{customerID}", method = RequestMethod.DELETE, produces = "application/json")
    @Transactional
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerID") long customerID) {
        Optional<Customer> customer = this.mainService.findCustomerByCustomerID(customerID);

        if (customer.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        this.mainService.deleteCustomerByCustomerID(customerID);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}
