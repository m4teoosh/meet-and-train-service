package pl.sportovo.domain.athlete.exception;

import pl.sportovo.exception.BusinessException;

public class AthleteWithProvidedUsernameAlreadyExistsException extends BusinessException {
    public AthleteWithProvidedUsernameAlreadyExistsException() {
        super("athlete-with-provided-username-already-exists");
    }
}
