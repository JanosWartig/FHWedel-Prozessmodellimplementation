package de.fhwedel.pimpl.repos;

import de.fhwedel.pimpl.model.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomCategoryRepo extends JpaRepository<RoomCategory, Integer> {

    @Query("SELECT rc FROM RoomCategory rc WHERE rc.name LIKE %:keyword%")
    List<RoomCategory> findByKeyword(@Param("keyword") String keyword);

}
