package de.fhwedel.pimpl.repos;

import de.fhwedel.pimpl.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepo extends JpaRepository<Claim, Integer> {

}


