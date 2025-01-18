package pl.sportovo.domain.athlete;

import pl.sportovo.exception.ErrorResponse;

import java.util.UUID;

@Deprecated
public class AthleteErrorResponse {

    //TODO unify errors
    public static ErrorResponse athleteExists() {
        return new ErrorResponse(
                UUID.randomUUID().toString(),
                "Athlete with this username already exists!",
                "post.athlete.username");
    }

    public static ErrorResponse identityExists() {
        return new ErrorResponse(
                UUID.randomUUID().toString(),
                "Athlete with provided identity already exists!",
                "post.athlete.username");
    }

}
