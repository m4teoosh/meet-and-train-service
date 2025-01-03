package pl.sportovo.domain.activity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.sportovo.domain.activity.dto.ActivityRequest;
import pl.sportovo.domain.athlete.AthleteService;
import pl.sportovo.domain.location.LocationService;

@Mapper(componentModel = "cdi", uses = {ActivityService.class, AthleteService.class, LocationService.class})
public interface ActivityMapper {

    @Mapping(target = "owner", source = "ownerId")
    @Mapping(target = "location", source = "locationId")
    Activity fromRequest(ActivityRequest activityRequest);

}
