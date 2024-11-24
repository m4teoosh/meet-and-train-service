package pl.sportovo.domain.athlete;

import pl.sportovo.error.ErrorResponse;

import java.util.UUID;

public class AthleteErrorResponse {
    public static ErrorResponse athleteExists() {
        return new ErrorResponse(
                UUID.randomUUID().toString(),
                "Athlete with this username already exists!",
                "post.athlete.username");
    }

}
