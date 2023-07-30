package de.fhwedel.pimpl.repos;

import de.fhwedel.pimpl.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepo extends JpaRepository<Guest, Integer> {



}

