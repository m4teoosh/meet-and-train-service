package pl.sportovo.domain.activity.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import pl.sportovo.domain.discipline.Discipline;

import java.util.UUID;

@Data
public class CreateActivity {

    @NotNull
    private Discipline discipline;

    @NotNull
    private String name;

    @NotNull
    private UUID ownerId;

    @NotNull
    private UUID locationId;

    @Range(min = 2, max = 20)
    private Integer capacity;

    @NotNull
    String startDateTime;

    @NotNull
    String endDateTime;

    private Boolean isPublic = true;

    private Boolean isRecurring = false;
}
