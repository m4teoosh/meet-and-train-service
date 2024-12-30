package pl.sportovo.domain.activity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import pl.sportovo.domain.activity.exception.ActivityLocationDoesNotExistException;
import pl.sportovo.domain.activity.exception.ActivityOwnerDoesNotExistException;
import pl.sportovo.domain.activity.model.Activity;
import pl.sportovo.domain.activity.model.CreateActivity;
import pl.sportovo.domain.athlete.Athlete;
import pl.sportovo.domain.athlete.AthleteService;
import pl.sportovo.domain.location.Location;
import pl.sportovo.domain.location.LocationService;

import java.time.LocalDateTime;

@ApplicationScoped
public class ActivityService {

    @Inject
    LocationService locationService;

    @Inject
    AthleteService athleteService;


    public Activity createActivity(@Valid CreateActivity createActivity) {
        Activity activity = new Activity();
        activity.setName(createActivity.getName());
        activity.setDiscipline(createActivity.getDiscipline());
        activity.setCapacity(createActivity.getCapacity());

        Location location = locationService.findById(createActivity.getLocationId());
        if (location == null) throw new ActivityLocationDoesNotExistException();
        activity.setLocation(location);

        Athlete athlete = athleteService.findById(createActivity.getOwnerId());
        if (athlete == null) throw new ActivityOwnerDoesNotExistException();
        activity.setOwner(athlete);

        activity.setStartDateTime(LocalDateTime.parse(createActivity.getStartDateTime()));
        activity.setEndDateTime(LocalDateTime.parse(createActivity.getEndDateTime()));
        activity.setIsPublic(createActivity.getIsPublic());
        activity.setIsRecurring(createActivity.getIsRecurring());
        activity.persist();

        if (activity.isPersistent())
            return activity;
        else
            return null;
    }
}
