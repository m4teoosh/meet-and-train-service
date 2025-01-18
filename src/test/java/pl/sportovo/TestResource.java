package pl.sportovo;

import pl.sportovo.domain.activity.dto.ActivityInput;
import pl.sportovo.domain.athlete.dto.AthleteInput;
import pl.sportovo.domain.athlete.dto.PublicAthlete;
import pl.sportovo.domain.discipline.Discipline;
import pl.sportovo.domain.location.Location;

import java.time.LocalDateTime;

import static pl.sportovo.TestUtils.post;

public class TestResource {

    public static ActivityInput createActivity(String ownerName, String locationName, double lat, double lon, String token) {
        AthleteInput ownerInput = new AthleteInput();
        ownerInput.setUsername(ownerName);

        PublicAthlete owner = post("/athletes", ownerInput, token).as(PublicAthlete.class);

        Location location = new Location();
        location.setName(locationName);
        location.setLatitude(lat);
        location.setLongitude(lon);

        location = post("/locations", location, token).as(Location.class);

        ActivityInput activityInput = new ActivityInput();
        activityInput.setName("Boxing Training");
        activityInput.setOwnerId(owner.getId());
        activityInput.setLocationId(location.getId());
        activityInput.setDiscipline(Discipline.BOXING);
        activityInput.setCapacity(20);
        activityInput.setStartDateTime(LocalDateTime.now().plusDays(1).toString());
        activityInput.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(1).toString());
        return activityInput;
    }

}
