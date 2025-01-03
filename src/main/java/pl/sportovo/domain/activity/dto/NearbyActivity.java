package pl.sportovo.domain.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.sportovo.domain.discipline.Discipline;

import java.util.UUID;

@AllArgsConstructor
@Data
public class NearbyActivity {
    private UUID id;
    private Discipline discipline;
    private String name;
    private Double distance;
}
