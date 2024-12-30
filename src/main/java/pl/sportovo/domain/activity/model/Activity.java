package pl.sportovo.domain.activity.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import pl.sportovo.domain.athlete.Athlete;
import pl.sportovo.domain.discipline.Discipline;
import pl.sportovo.domain.location.Location;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Activity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ACTIVITY_ID")
    UUID id;

    @NotNull
    String name;

    @NotNull
    @ManyToOne
    private Athlete owner;

    @NotNull
    private Discipline discipline;

    @Range(min = 2, max = 20)
    private Integer capacity;

    @NotNull
    @ManyToOne
    private Location location;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    @OneToMany
    private List<Athlete> participants;

    @NotNull
    private Boolean isPublic = true;

    @NotNull
    private Boolean isRecurring = false;
}
