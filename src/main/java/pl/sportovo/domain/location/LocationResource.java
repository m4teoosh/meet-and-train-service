package pl.sportovo.domain.location;

import io.quarkus.security.Authenticated;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.sportovo.domain.activity.Activity;

import java.net.URI;

@Path("/locations")
@Authenticated
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class LocationResource {

    @GET
    @Path("{id}")
    public Activity get(@PathParam("id") Long id){
        return Location.findById(id);
    }

    @Transactional
    @POST
    public Response post(@Valid Location location) {
        location.persist();

        if (location.isPersistent()) {
            return Response.created(URI.create("/locations/" + location.getId())).entity(location).build();
        } else {
            return Response.status(400).build();
        }
    }
}
