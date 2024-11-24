package pl.sportovo.domain.athlete;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.sportovo.domain.activity.Activity;

import java.net.URI;
import java.util.List;

@Path("/athletes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AthleteResource {

    @GET
    public List<Activity> list(){
        return Athlete.listAll();
    }

    @GET
    @Path("{id}")
    public Activity get(@PathParam("id") Long id){
        return Athlete.findById(id);
    }

    @Transactional
    @POST
    public Response post(@Valid Athlete athlete) {
        String username = athlete.getUsername();
        if (Athlete.usernameExists(username)) {
            return Response.status(400).entity(AthleteErrorResponse.athleteExists()).build();
        }

        athlete.persist();

        if (athlete.isPersistent()) {
            return Response.created(URI.create("/athletes/" + athlete.id)).entity(athlete).build();
        } else {
            return Response.status(400).build();
        }
    }
}
