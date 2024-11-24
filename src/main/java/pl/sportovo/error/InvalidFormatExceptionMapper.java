package pl.sportovo.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.UUID;

@Provider
public class InvalidFormatExceptionMapper implements ExceptionMapper<InvalidFormatException> {

        @Override
        public Response toResponse(InvalidFormatException exception) {
            StringBuilder message = new StringBuilder();
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new ErrorResponse(UUID.randomUUID().toString(), new ErrorResponse.ErrorMessage(exception.getOriginalMessage()))).build();
        }
}
