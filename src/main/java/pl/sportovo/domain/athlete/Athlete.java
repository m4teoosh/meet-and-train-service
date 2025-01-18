package pl.sportovo.domain.athlete;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Athlete extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String subjectId;

    @NotBlank
    @Column(unique = true)
    @Length(min = 3, max = 20)
    private String username;

    @Size(max = 255)
    private String bio;

    public static boolean usernameExists(String username) {
        return Athlete.find("username", username).count() > 0;
    }
    public static boolean subjectIdExists(String oidcId) {
        return Athlete.find("subjectId", oidcId).count() > 0;
    }
    public static Athlete findByUsername(String username) {
        return Athlete.find("username", username).firstResult();
    }
    public static Athlete findBySubjectId(String oidcId) {
        return Athlete.find("identityId", oidcId).firstResult();
    }

    public static boolean isLoggedInUserAthlete(UUID id, String oidcId) {
        return Athlete.find("id = ?1 and subjectId = ?2", id, oidcId).count() > 0;
    }
}
