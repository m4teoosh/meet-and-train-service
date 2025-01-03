package pl.sportovo.domain.activity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pl.sportovo.domain.discipline.Discipline;

import java.util.UUID;


@Data
public class ActivityRequest {
    @NotNull Discipline discipline;
    @NotNull String name;
    @NotNull UUID ownerId;
    @NotNull UUID locationId;
    @NotNull Integer capacity;
    @NotNull String startDateTime;
    @NotNull String endDateTime;
    Boolean isPublic = true;
    Boolean isRecurring = false;
}
