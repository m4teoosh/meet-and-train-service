package pl.sportovo.domain.activity;

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
import pl.sportovo.domain.activity.dto.ActivityInput;
import pl.sportovo.domain.activity.dto.PublicActivity;
import pl.sportovo.domain.athlete.AthleteService;

import java.net.URI;
import java.util.UUID;

@Path("/activities")
@Authenticated
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ActivityResource {

    @Inject
    SecurityIdentity identity;
    @Inject
    ActivityService activityService;
    @Inject
    AthleteService athleteService;

    @GET
    @Path("/nearby")
    @Operation(summary = "List nearby activities", description = "Returns a list of activities near the specified location")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "List of nearby activities"),
            @APIResponse(responseCode = "400", description = "Invalid input parameters")
    })
    public Response listNearby(@QueryParam("latitude") Double latitude,
                               @QueryParam("longitude") Double longitude,
                               @QueryParam("radius") Integer radius) {
        return Response.ok().entity(activityService.findNearbyActivities(latitude, longitude, radius)).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Get activity by ID", description = "Returns an activity by its ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Activity found"),
            @APIResponse(responseCode = "404", description = "Activity not found")
    })
    public Response get(@PathParam("id") UUID id) {
        PublicActivity activity = activityService.getActivityDetails(id);
        if (activity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(activity).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a new activity", description = "Creates a new activity with the provided details")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Activity created"),
            @APIResponse(responseCode = "400", description = "Invalid input"),
    })
    public Response post(@Valid ActivityInput activityInput) {
        Activity activity = activityService.createActivity(activityInput);
        URI location = UriBuilder.fromResource(ActivityResource.class)
                .path("{id}")
                .build(activity.getId());

        return Response.created(location).entity(activity).build();
    }

    @POST
    @Path("{id}/join")
    @Transactional
    @Operation(summary = "Join an activity", description = "Allows an athlete to join an activity by its ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Joined activity"),
            @APIResponse(responseCode = "404", description = "Activity not found")
    })
    public Response join(@PathParam("id") UUID id) {
        Activity activity = Activity.findById(id);
        if (activity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        UUID athleteId = athleteService.getAthleteIdBySubjectId(identity.getPrincipal().getName());
        activityService.joinActivity(id, athleteId);
        return Response.ok().entity(activity).build();
    }

    @DELETE
    @Path("{id}/leave")
    @Transactional
    @Operation(summary = "Leave an activity", description = "Allows an athlete to leave an activity by its ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Left activity"),
            @APIResponse(responseCode = "404", description = "Activity not found")
    })
    public Response leave(@PathParam("id") UUID id) {
        Activity activity = Activity.findById(id);
        if (activity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        UUID athleteId = athleteService.getAthleteIdBySubjectId(identity.getPrincipal().getName());
        activityService.leaveActivity(id, athleteId);
        return Response.ok().entity(activity).build();
    }
}