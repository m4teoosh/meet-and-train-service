package pl.sportovo.domain.location;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class LocationService {
    public Location findById(UUID id) {
        return Location.findById(id);
    }
}
