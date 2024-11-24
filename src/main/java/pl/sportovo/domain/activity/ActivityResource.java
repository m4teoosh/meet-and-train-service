package pl.sportovo.domain.activity;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/activities")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ActivityResource {
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
    public Response post(@Valid Activity activity) {
        activity.persist();

        if (activity.isPersistent()) {
            return Response.created(URI.create("/activities/" + activity.id)).entity(activity).build();
        } else {
            return Response.status(400).build();
        }
    }



}
