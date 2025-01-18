package pl.sportovo.exception;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SecurityExceptionMapper implements ExceptionMapper<SecurityException> {

    @Override
    public Response toResponse(SecurityException exception) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity("You are not allowed to access this resource")
                .build();
    }
}