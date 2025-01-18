package pl.sportovo.domain.athlete;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import pl.sportovo.domain.activity.Activity;
import pl.sportovo.domain.activity.ActivityService;
import pl.sportovo.domain.athlete.dto.AthleteInput;
import pl.sportovo.domain.athlete.dto.PublicAthlete;
import pl.sportovo.security.OwnerOnly;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/athletes")
@Authenticated
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AthleteResource {

    @Inject
    SecurityIdentity identity;
    @Inject
    AthleteService athleteService;
    @Inject
    ActivityService activityService;

    @GET
    @Path("{username}")
    @Operation(summary = "Get public athlete profile by username" , description = "Returns an athlete by their ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Athlete found"),
            @APIResponse(responseCode = "404", description = "Athlete not found")
    })
    public PublicAthlete getPublicAthleteProfile(@PathParam("username") String username) {
        return athleteService.findPublicAthleteByUsername(username);
    }

    @GET
    @Path("{id}/activities")
    @OwnerOnly
    @Operation(summary = "List joined activities", description = "Returns a list of activities joined by the athlete")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "List of joined activities"),
            @APIResponse(responseCode = "404", description = "Athlete not found")
    })
    public Response joinedActivities(@PathParam("id") UUID id) {
        List<Activity> activities = activityService.listActivitiesByParticipant(id);
        return Response.ok().entity(activities).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Register a new athlete", description = "Creates a new athlete with the provided details")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Athlete created"),
            @APIResponse(responseCode = "400", description = "Invalid input"),
            @APIResponse(responseCode = "400", description = "Athlete with provided identity or username already exists")
    })
    public Response register(@Valid AthleteInput athleteInput) {
        String subjectId = identity.getPrincipal().getName();
        PublicAthlete athlete = athleteService.registerAthlete(athleteInput, subjectId);

        URI location = UriBuilder.fromResource(AthleteResource.class)
                .path("{username}")
                .build(athlete.getUsername());

        return Response.created(location).entity(athlete).build();
    }
}