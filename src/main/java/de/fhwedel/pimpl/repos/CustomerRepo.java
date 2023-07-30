package de.fhwedel.pimpl.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.fhwedel.pimpl.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    List<Customer> findBySurnameContainingIgnoreCase(String surname);
}
