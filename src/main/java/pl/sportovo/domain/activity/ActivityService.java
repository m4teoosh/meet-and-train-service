package pl.sportovo.domain.activity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import pl.sportovo.domain.activity.model.Activity;
import pl.sportovo.domain.activity.model.CreateActivity;
import pl.sportovo.domain.athlete.Athlete;
import pl.sportovo.domain.athlete.AthleteService;
import pl.sportovo.domain.location.Location;
import pl.sportovo.domain.location.LocationService;

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
        activity.setLocation(location);

        Athlete athlete = athleteService.findById(createActivity.getOwnerId());
        activity.setOwner(athlete);

        activity.setIsPublic(createActivity.getIsPublic());
        activity.persist();

        if (activity.isPersistent())
            return activity;
        else
            return null;
    }
}
