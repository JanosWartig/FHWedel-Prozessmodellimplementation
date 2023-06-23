package de.fhwedel.pimpl.repos;

import de.fhwedel.pimpl.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepo extends JpaRepository<Booking, Integer> {

}
