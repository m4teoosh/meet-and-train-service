package pl.sportovo.domain;

import io.quarkus.oidc.IdToken;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/login")
@Authenticated
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class LoginResource {

    @IdToken
    @Inject
    JsonWebToken idToken;

    @GET
    public String get() {
        return """
                {
                    "token": "%s",
                    "details": "%s"
                }
                """.formatted(
                idToken.getRawToken(),
                idToken);
    }

}
