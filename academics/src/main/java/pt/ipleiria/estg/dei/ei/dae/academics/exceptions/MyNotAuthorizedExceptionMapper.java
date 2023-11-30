package pt.ipleiria.estg.dei.ei.dae.academics.exceptions;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class MyNotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {

    private static final java.util.logging.Logger Logger = java.util.logging.Logger.getLogger(MyEntityExistsException.class.getCanonicalName());

    @Override
    public Response toResponse(NotAuthorizedException e) {
        String errorMsg = e.getMessage();
        Logger.warning("ERROR: " + errorMsg);
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorMsg)
                .build();
    }
}
