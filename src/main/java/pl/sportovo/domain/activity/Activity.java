package pl.sportovo.domain.activity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import pl.sportovo.domain.athlete.Athlete;
import pl.sportovo.domain.discipline.Discipline;
import pl.sportovo.domain.location.Location;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Activity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    private Discipline discipline;

    @Range(min = 2, max = 20)
    private Integer capacity;

    @NotNull
    @ManyToOne
    private Location location;

    @NotNull
    @ManyToOne
    private Athlete owner;

    @OneToMany
    private List<Athlete> participants;

    @NotNull
    private Boolean isPublic = true;
}
