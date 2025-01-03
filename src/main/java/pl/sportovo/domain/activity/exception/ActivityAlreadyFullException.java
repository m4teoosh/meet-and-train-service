package pl.sportovo.domain.activity.exception;

import pl.sportovo.exception.BusinessException;

public class ActivityAlreadyFullException extends BusinessException {
    public ActivityAlreadyFullException() {
        super("activity-already-full");
    }
}
