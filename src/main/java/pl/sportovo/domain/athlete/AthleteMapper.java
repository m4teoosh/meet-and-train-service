package pl.sportovo.domain.athlete;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import pl.sportovo.domain.athlete.dto.AthleteInput;
import pl.sportovo.domain.athlete.dto.PublicAthlete;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface AthleteMapper {

    PublicAthlete toPublicAthlete(Athlete athlete);

    default Athlete fromAthleteInput(AthleteInput athleteInput, String subjectId) {
        Athlete athlete = new Athlete();
        athlete.setSubjectId(subjectId);
        athlete.setUsername(athleteInput.getUsername());
        athlete.setBio(athleteInput.getBio());
        return athlete;
    }


}
