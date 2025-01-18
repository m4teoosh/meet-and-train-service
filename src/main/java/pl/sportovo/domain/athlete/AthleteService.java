package pl.sportovo.domain.athlete;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pl.sportovo.domain.athlete.dto.AthleteInput;
import pl.sportovo.domain.athlete.dto.PublicAthlete;
import pl.sportovo.domain.athlete.exception.AthleteWithProvidedIdentityAlreadyExistsException;
import pl.sportovo.domain.athlete.exception.AthleteWithProvidedUsernameAlreadyExistsException;

import java.util.UUID;

@ApplicationScoped
public class AthleteService {

    @Inject
    AthleteMapper athleteMapper;

    public Athlete findById(UUID id) {
        return Athlete.findById(id);
    }
    public boolean isLoggedInUserAthlete(UUID id, String oidcId) {
        return Athlete.isLoggedInUserAthlete(id, oidcId);
    }
    public UUID getAthleteIdBySubjectId(String subjectId) {
        return Athlete.findBySubjectId(subjectId).getId();
    }
    public PublicAthlete registerAthlete(AthleteInput athleteInput, String subjectId) {
        if (Athlete.subjectIdExists(subjectId)) {
           throw new AthleteWithProvidedIdentityAlreadyExistsException();
        }

        if (Athlete.usernameExists(athleteInput.getUsername())) {
            throw new AthleteWithProvidedUsernameAlreadyExistsException();
        }

        Athlete athlete = athleteMapper.fromAthleteInput(athleteInput, subjectId);
        athlete.persist();

        return athleteMapper.toPublicAthlete(athlete);
    }

    public PublicAthlete findPublicAthleteByUsername(String username) {
        Athlete athlete = Athlete.findByUsername(username);
        return athleteMapper.toPublicAthlete(athlete);
    }

    public Athlete findAthleteByUsername(String username) {
        return  Athlete.findByUsername(username);
    }
}
