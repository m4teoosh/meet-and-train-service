package pl.sportovo.domain.team;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/teams")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class TeamResource {

    @GET
    public List<Team> list() {
        return Team.listAll();
    }

    @GET
    @Path("{id}")
    public Team get(@PathParam("id") UUID id) {
        Team team = Team.findById(id);
        if (team == null) {
            throw new WebApplicationException("Team not found", Response.Status.NOT_FOUND);
        }
        return team;
    }

    @Transactional
    @POST
    public Response post(@Valid Team team) {
        if (Team.nameExists(team.getName())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(TeamErrorResponse.teamNameExists())
                    .build();
        }

        team.persist();

        if (team.isPersistent()) {
            return Response.created(URI.create("/teams/" + team.id)).entity(team).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Transactional
    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") UUID id, @Valid Team updatedTeam) {
        Team existingTeam = Team.findById(id);
        if (existingTeam == null) {
            throw new WebApplicationException("Team not found", Response.Status.NOT_FOUND);
        }

        if (!existingTeam.getName().equals(updatedTeam.getName()) && Team.nameExists(updatedTeam.getName())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(TeamErrorResponse.teamNameExists())
                    .build();
        }

        existingTeam.setName(updatedTeam.getName());
        existingTeam.setDiscipline(updatedTeam.getDiscipline());
        existingTeam.setIsInviteOnly(updatedTeam.getIsInviteOnly());

        return Response.ok(existingTeam).build();
    }

    @Transactional
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        Team team = Team.findById(id);
        if (team == null) {
            throw new WebApplicationException("Team not found", Response.Status.NOT_FOUND);
        }

        team.delete();
        return Response.noContent().build();
    }
}
