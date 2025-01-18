package pl.sportovo.domain.activity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import pl.sportovo.domain.activity.dto.ActivityInput;
import pl.sportovo.domain.activity.dto.NearbyActivity;
import pl.sportovo.domain.activity.dto.PublicActivity;
import pl.sportovo.domain.activity.exception.ActivityAlreadyFullException;
import pl.sportovo.domain.activity.exception.ActivityLocationDoesNotExistException;
import pl.sportovo.domain.activity.exception.ActivityOwnerDoesNotExistException;
import pl.sportovo.domain.activity.exception.AthleteAlreadyJoinedTheActivity;
import pl.sportovo.domain.athlete.Athlete;
import pl.sportovo.domain.athlete.AthleteService;
import pl.sportovo.domain.location.Location;
import pl.sportovo.domain.location.LocationService;
import pl.sportovo.geography.GeoCalculator;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ActivityService {

    @Inject
    LocationService locationService;
    @Inject
    AthleteService athleteService;
    @Inject
    ActivityMapper activityMapper;

    public PublicActivity getActivityDetails(UUID id) {
        Activity activity = Activity.findById(id);
        return activityMapper.toPublicActivity(activity);
    }

    public Activity createActivity(@Valid ActivityInput activityInput) {
        Location location = locationService.findById(activityInput.getLocationId());
        if (location == null) throw new ActivityLocationDoesNotExistException();

        Athlete athlete = athleteService.findById(activityInput.getOwnerId());
        if (athlete == null) throw new ActivityOwnerDoesNotExistException();

        Activity activity = activityMapper.fromActivityInput(activityInput);
        activity.persist();
        return activity;
    }

    public PublicActivity joinActivity(UUID activityId, UUID athleteId) {
        Activity activity = Activity.findById(activityId);
        Athlete athlete = Athlete.findById(athleteId);

        if (activity != null && athlete != null) {
            if (activity.getParticipants().size() >= activity.getCapacity()) {
                throw new ActivityAlreadyFullException();
            }
            if (activity.getParticipants().contains(athlete)) {
                throw new AthleteAlreadyJoinedTheActivity();
            }
            activity.getParticipants().add(athlete);
            activity.persist();
            return activityMapper.toPublicActivity(activity);
        } else {
            return null;
        }
    }

    public PublicActivity leaveActivity(UUID activityId, UUID athleteId) {
        Activity activity = Activity.findById(activityId);
        Athlete athlete = Athlete.findById(athleteId);

        if (activity != null && athlete != null) {
            activity.getParticipants().remove(athlete);
            activity.persist();
            return activityMapper.toPublicActivity(activity);
        } else {
            return null;
        }
    }

    public List<NearbyActivity> findNearbyActivities(double latitude, double longitude, int radiusInKilometers) {
        List<Activity> activities = Activity.findNearbyActivities(latitude, longitude, radiusInKilometers);
        return activities.stream()
                .map(activity -> {
                    double distance = GeoCalculator.calculateDistanceInKilometers(latitude, longitude, activity.getLocation().getLatitude(), activity.getLocation().getLongitude());
                    return new NearbyActivity(activity.getId(), activity.getDiscipline(), activity.getName(), distance);
                })
                .toList();
    }

    public List<Activity> listActivitiesByParticipant(UUID id) {
        return Activity.findByParticipantId(id);
    }

    public PublicActivity findActivityById(UUID id) {
        Activity activity = Activity.findById(id);
        return activityMapper.toPublicActivity(activity);
    }
}
