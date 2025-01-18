package pl.sportovo.domain.activity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import pl.sportovo.domain.activity.dto.ActivityInput;
import pl.sportovo.domain.athlete.AthleteService;
import pl.sportovo.domain.location.LocationService;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, uses = {ActivityService.class, AthleteService.class, LocationService.class})
public interface ActivityMapper {

    @Mapping(target = "owner", source = "ownerId")
    @Mapping(target = "location", source = "locationId")
    Activity fromRequest(ActivityInput activityInput);

}
