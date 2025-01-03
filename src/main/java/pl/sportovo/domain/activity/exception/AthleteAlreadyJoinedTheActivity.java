package pl.sportovo.domain.activity.exception;

import pl.sportovo.exception.BusinessException;

    public class AthleteAlreadyJoinedTheActivity extends BusinessException {

    public AthleteAlreadyJoinedTheActivity() {
        super("athlete-already-joined-the-activity");
    }
}
