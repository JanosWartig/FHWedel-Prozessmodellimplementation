package de.fhwedel.pimpl.repos;

import de.fhwedel.pimpl.model.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomCategoryRepo extends JpaRepository<RoomCategory, Integer> {

}
