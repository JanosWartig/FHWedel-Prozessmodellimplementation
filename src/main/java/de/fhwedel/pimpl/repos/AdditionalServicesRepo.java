package de.fhwedel.pimpl.repos;

import de.fhwedel.pimpl.model.AdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalServicesRepo extends JpaRepository<AdditionalService, Integer> {

}

