package de.fhwedel.pimpl.repos;

import de.fhwedel.pimpl.model.Room;
import de.fhwedel.pimpl.model.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepo extends JpaRepository<Room, Integer> {

    List<Room> findByRoomCategoryId(Integer roomCategoryId);

}
