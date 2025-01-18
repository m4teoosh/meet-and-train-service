package pl.sportovo.domain.activity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import pl.sportovo.domain.activity.dto.ActivityInput;
import pl.sportovo.domain.activity.dto.PublicActivity;
import pl.sportovo.domain.athlete.Athlete;
import pl.sportovo.domain.athlete.AthleteService;
import pl.sportovo.domain.location.LocationService;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, uses = {ActivityService.class, AthleteService.class, LocationService.class})
public interface ActivityMapper {

    @Mapping(target = "owner", source = "ownerId")
    @Mapping(target = "location", source = "locationId")
    Activity fromActivityInput(ActivityInput activityInput);

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "participantsNumber", source = "participants")
    PublicActivity toPublicActivity(Activity activity);

    default Integer participantsNumber(List<Athlete> participants) {
        return participants.size();
    }
}
