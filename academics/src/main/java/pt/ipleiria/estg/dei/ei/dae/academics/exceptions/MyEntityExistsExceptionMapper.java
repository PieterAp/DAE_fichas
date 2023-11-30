package pt.ipleiria.estg.dei.ei.dae.academics.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Logger;

@Provider
public class MyEntityExistsExceptionMapper implements ExceptionMapper<MyEntityExistsException> {

    private static final Logger Logger = java.util.logging.Logger.getLogger(MyEntityExistsException.class.getCanonicalName());

    @Override
    public Response toResponse(MyEntityExistsException e) {
        String errorMsg = e.getMessage();
        Logger.warning("ERROR: " + errorMsg);
        return Response.status(Response.Status.CONFLICT)
            .entity(errorMsg)
            .build();
    }
}
