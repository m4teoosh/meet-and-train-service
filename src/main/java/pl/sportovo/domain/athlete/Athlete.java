package pl.sportovo.domain.athlete;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Athlete extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(unique = true)
    private String username;

    public static boolean usernameExists(String username) {
        return Athlete.find("username", username).count() > 0;
    }

    public static Athlete findByUsername(String username) {
        return Athlete.find("username", username).firstResult();
    }
}
