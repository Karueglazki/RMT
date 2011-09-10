package de.flower.rmt.service;

import de.flower.rmt.model.Venue;
import de.flower.rmt.model.Venue_;
import de.flower.rmt.repository.IVenueRepo;
import de.flower.rmt.repository.Specs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.Attribute;
import java.util.List;

/**
 * @author oblume
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class VenueManager extends AbstractService implements IVenueManager {

    @Autowired
    private IVenueRepo venueRepo;

    @Override
    @Transactional(readOnly = false)
    public void save(Venue venue) {
        venueRepo.save(venue);
    }

    @Override
    public List<Venue> findAll(final Attribute... attributes) {
        Specification hasClub = Specs.eq(Venue_.club, getClub());
        List<Venue> list = venueRepo.findAll(hasClub);
        return list;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Venue venue) {
        throw new UnsupportedOperationException("Feature not implemented!");
    }

    @Override
    public Venue newVenueInstance() {

        Venue venue = new Venue(getClub());
        return venue;
    }
}