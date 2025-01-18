package pl.sportovo.security;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pl.sportovo.domain.athlete.AthleteService;

import java.util.UUID;

@ApplicationScoped
public class AuthorizationService {

    @Inject
    AthleteService athleteService;

    public boolean isOwner(UUID athleteId, SecurityIdentity securityIdentity) {
        String subject = securityIdentity.getPrincipal().getName();
        return athleteService.isLoggedInUserAthlete(athleteId, subject);
    }
}
