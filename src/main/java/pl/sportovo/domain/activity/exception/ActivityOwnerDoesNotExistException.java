package pl.sportovo.domain.activity.exception;

import pl.sportovo.exception.BusinessException;

public class ActivityOwnerDoesNotExistException extends BusinessException {

    public ActivityOwnerDoesNotExistException() {
        super("activity-owner-does-not-exist");
    }
}
