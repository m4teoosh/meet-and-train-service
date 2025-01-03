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
import pl.sportovo.geography.GeoCalculator;

import java.time.LocalDateTime;
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
    String name;

    @NotNull
    @ManyToOne
    private Athlete owner;

    @NotNull
    @ManyToOne
    private Location location;

    @NotNull
    private Discipline discipline;

    @Range(min = 2, max = 20)
    private Integer capacity;

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

    public static Activity findFirstByName(String name) {
        return find("name", name).firstResult();
    }

    public static List<Activity> findByParticipantId(UUID participantId) {
        return find("SELECT a FROM Activity a JOIN a.participants p WHERE p.id = ?1", participantId).list();
    }

    public static List<Activity> findNearbyActivities(double latitude, double longitude, int radiusInKilometers) {
        double latitudeRange = GeoCalculator.covertKilometersToLatitudeDegrees(radiusInKilometers);
        double longitudeRange = GeoCalculator.covertKilometersToLongitudeDegrees(latitude, radiusInKilometers);

        return find("SELECT a FROM Activity a WHERE a.location.latitude BETWEEN ?1 AND ?2 AND a.location.longitude BETWEEN ?3 AND ?4",
                latitude - latitudeRange, latitude + latitudeRange, longitude - longitudeRange, longitude + longitudeRange).list();
    }
}
