package pl.sportovo.domain.athlete.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AthleteInput {
    @NotNull private String username;
    private String bio;
}
