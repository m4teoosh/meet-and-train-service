package pl.sportovo.domain.athlete;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import pl.sportovo.domain.athlete.dto.PublicAthlete;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface AthleteMapper {

    PublicAthlete toPublicAthlete(Athlete athlete);


}
