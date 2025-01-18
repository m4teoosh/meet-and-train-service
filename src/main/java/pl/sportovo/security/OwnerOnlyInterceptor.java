package pl.sportovo.security;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.PathParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.UUID;

@OwnerOnly
@Interceptor
public class OwnerOnlyInterceptor {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    AuthorizationService authorizationService;

    @AroundInvoke
    public Object checkPermission(InvocationContext context) throws Exception {
        UUID athleteId = getAthleteIdFromParams(context);
        if (!authorizationService.isOwner(athleteId, securityIdentity)) {
            throw new SecurityException("You are not allowed to access this resource");
        }
        return context.proceed();
    }

    private UUID getAthleteIdFromParams(InvocationContext context) {
        Parameter[] parameters = context.getMethod().getParameters();
        Object[] parameterValues = context.getParameters();
        OwnerOnly ownerOnly = context.getMethod().getAnnotation(OwnerOnly.class);

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Annotation[] annotations = parameter.getAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation instanceof PathParam pathParam && pathParam.value().equals(ownerOnly.parameterName())) {
                    if (parameterValues[i] instanceof UUID) {
                        return (UUID) parameterValues[i];
                    }
                }
            }
        }
        return null;
    }

}
