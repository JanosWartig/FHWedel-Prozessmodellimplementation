package de.fhwedel.pimpl.repos;

import de.fhwedel.pimpl.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomCategoryRepo extends JpaRepository<Customer, Integer> {


}
