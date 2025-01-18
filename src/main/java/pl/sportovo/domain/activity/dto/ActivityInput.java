package pl.sportovo.domain.activity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pl.sportovo.domain.discipline.Discipline;

import java.util.UUID;


@Data
public class ActivityInput {
    private @NotNull Discipline discipline;
    private @NotNull String name;
    private @NotNull UUID ownerId;
    private @NotNull UUID locationId;
    private @NotNull Integer capacity;
    private @NotNull String startDateTime;
    private @NotNull String endDateTime;
    private Boolean isPublic = true;
    private Boolean isRecurring = false;
}
