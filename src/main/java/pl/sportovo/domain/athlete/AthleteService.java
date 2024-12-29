package pl.sportovo.domain.athlete;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AthleteService {
    public Athlete findById(UUID id) {
        return Athlete.findById(id);
    }
}
