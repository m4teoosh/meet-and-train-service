package pl.sportovo.domain.team;

import pl.sportovo.exception.ErrorResponse;

import java.util.UUID;

public class TeamErrorResponse {

    public static ErrorResponse teamNameExists() {
        return new ErrorResponse(
                UUID.randomUUID().toString(),
                "A team with this name already exists!",
                "post.team.name");
    }

}
