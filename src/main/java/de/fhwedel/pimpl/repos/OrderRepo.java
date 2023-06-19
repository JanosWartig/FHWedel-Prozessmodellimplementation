package de.fhwedel.pimpl.repos;

import de.fhwedel.pimpl.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Integer> {

    List<Order> findByCreateDate(LocalDate date);

}
