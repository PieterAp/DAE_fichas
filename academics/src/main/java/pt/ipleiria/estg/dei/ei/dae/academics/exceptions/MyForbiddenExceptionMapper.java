package pt.ipleiria.estg.dei.ei.dae.academics.exceptions;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class MyForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    private static final java.util.logging.Logger Logger = java.util.logging.Logger.getLogger(MyEntityExistsException.class.getCanonicalName());

    @Override
    public Response toResponse(ForbiddenException e) {
        String errorMsg = e.getMessage();
        Logger.warning("ERROR: " + errorMsg);
        return Response.status(Response.Status.FORBIDDEN)
                .entity(errorMsg)
                .build();
    }
}
