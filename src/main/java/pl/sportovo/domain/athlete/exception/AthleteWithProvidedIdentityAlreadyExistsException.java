package pl.sportovo.domain.athlete.exception;

import pl.sportovo.exception.BusinessException;

public class AthleteWithProvidedIdentityAlreadyExistsException extends BusinessException {

    public AthleteWithProvidedIdentityAlreadyExistsException() {
        super("athlete-with-provided-identity-already-exists");
    }
}
