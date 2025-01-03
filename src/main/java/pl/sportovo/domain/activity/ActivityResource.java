package pl.sportovo.domain.activity;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.sportovo.domain.activity.dto.ActivityRequest;

import java.net.URI;
import java.util.UUID;

@Path("/activities")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ActivityResource {
    @Inject
    ActivityService activityService;

    @GET
    public Response list(@QueryParam("participantId") UUID participantId) {
        return Response.ok().entity(Activity.listAll()).build();
    }

    @GET
    @Path("/nearby")
    public Response listNearby(@QueryParam("latitude") Double latitude,
                               @QueryParam("longitude") Double longitude,
                               @QueryParam("radius") Integer radius) {
        return Response.ok().entity(activityService.findNearbyActivities(latitude, longitude, radius)).build();
    }

    @GET
    @Path("{id}")
    public Activity get(@PathParam("id") Long id) {
        return Activity.findById(id);
    }

    @Transactional
    @POST
    public Response post(@Valid ActivityRequest activityRequest) {
        Activity activity = activityService.createActivity(activityRequest);

        if (activity != null) {
            return Response.created(URI.create("/activities/" + activity.getId())).entity(activity).build();
        } else {
            return Response.status(400).build();
        }
    }

    @Transactional
    @POST
    @Path("{id}/join/{athleteId}")
    public Response join(@PathParam("id") UUID id, @PathParam("athleteId") UUID athleteId) {
        Activity activity = Activity.findById(id);
        if (activity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        activityService.joinActivity(id, athleteId);
        return Response.ok().entity(activity).build();
    }

    @Transactional
    @DELETE
    @Path("{id}/leave/{athleteId}")
    public Response leave(@PathParam("id") UUID id, @PathParam("athleteId") UUID athleteId) {
        Activity activity = Activity.findById(id);
        if (activity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        activityService.leaveActivity(id, athleteId);
        return Response.ok().entity(activity).build();
    }


}
