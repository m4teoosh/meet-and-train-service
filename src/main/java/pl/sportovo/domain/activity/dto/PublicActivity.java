package pl.sportovo.domain.activity.dto;


import lombok.Data;
import pl.sportovo.domain.athlete.dto.PublicAthlete;
import pl.sportovo.domain.discipline.Discipline;
import pl.sportovo.domain.location.Location;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PublicActivity {
    private UUID id;
    private String name;
    private PublicAthlete owner;
    private Location location;
    private Discipline discipline;
    private Integer participantsNumber;
    private Integer capacity;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Boolean isPublic = true;
    private Boolean isRecurring = false;
}
