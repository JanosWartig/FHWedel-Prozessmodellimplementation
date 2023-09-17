package de.fhwedel.pimpl.repos;

import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.model.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomCategoryRepo extends JpaRepository<RoomCategory, Integer> {

    List<RoomCategory> findByNameContainingIgnoreCase(String name);

    List<RoomCategory> findByNumberOfBeds(int numberOfBeds);

}
