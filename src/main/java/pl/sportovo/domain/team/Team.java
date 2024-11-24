package pl.sportovo.domain.team;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.sportovo.domain.athlete.Athlete;
import pl.sportovo.domain.discipline.Discipline;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Team extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(unique = true)
    String name;

    @NotNull Discipline discipline;

    Boolean isInviteOnly = false;

    public static boolean nameExists(String username) {
        return Team.find("name", username).count() > 0;
    }

}
