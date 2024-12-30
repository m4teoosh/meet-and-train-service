package pl.sportovo.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {
    @Override
    public Response toResponse(BusinessException exception) {
        List<ErrorResponse.ErrorMessage> errorMessages = exception.getSids().stream()
                .map(ErrorResponse.ErrorMessage::new)
                .collect(Collectors.toList());
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(UUID.randomUUID().toString(), errorMessages)).build();
    }
}
