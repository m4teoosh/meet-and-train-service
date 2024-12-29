package pl.sportovo.domain.activity.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import pl.sportovo.domain.discipline.Discipline;

import java.util.UUID;

@Data
public class CreateActivity {
        @NotNull
        private String name;

        private Discipline discipline;

        @Range(min = 2, max = 20)
        private Integer capacity;

        @NotNull
        private UUID locationId;

        @NotNull
        private UUID ownerId;

        private Boolean isPublic = true;

}
