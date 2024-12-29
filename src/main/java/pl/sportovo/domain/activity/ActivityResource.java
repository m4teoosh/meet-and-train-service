package pl.sportovo.domain.activity;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.sportovo.domain.activity.model.Activity;
import pl.sportovo.domain.activity.model.CreateActivity;

import java.net.URI;
import java.util.List;

@Path("/activities")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ActivityResource {

    @Inject
    ActivityService activityService;

    @GET
    public List<Activity> list(){
        return Activity.listAll();
    }

    @GET
    @Path("{id}")
    public Activity get(@PathParam("id") Long id){
        return Activity.findById(id);
    }

    @Transactional
    @POST
    public Response post(@Valid CreateActivity createActivity) {
        Activity activity = activityService.createActivity(createActivity);

        if (activity != null) {
            return Response.created(URI.create("/activities/" + activity.getId())).entity(createActivity).build();
        } else {
            return Response.status(400).build();
        }
    }



}
