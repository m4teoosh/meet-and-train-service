package pl.sportovo.domain.activity.exception;

import pl.sportovo.exception.BusinessException;

public class ActivityLocationDoesNotExistException extends BusinessException {

    public ActivityLocationDoesNotExistException() {
        super("activity-location-does-not-exist");
    }
}
