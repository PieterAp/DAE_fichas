package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.AuthDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.AuthDTOpasswordSet;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.User;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.academics.security.TokenIssuer;

@Path("auth") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class AuthService {
    @Inject
    private TokenIssuer issuer;

    @Context
    private SecurityContext securityContext;

    @EJB
    private UserBean userBean;

    protected static final byte[] SECRET_KEY = "veRysup3rstr0nginv1ncible5ecretkeY@academics.dae.ipleiria".getBytes();

    protected static final String ALGORITHM = "HMACSHA384";

    public static final long EXPIRY_MINS = 60L;

    @GET
    @Authenticated
    @Path("/user")
    public Response getAuthenticatedUser() {
        var username = securityContext.getUserPrincipal().getName();
        var user = userBean.findOrFail(username);
        return Response.ok(UserDTO.from(user)).build();
    }

    @POST
    @Path("/login")
    public Response authenticate(@Valid AuthDTO auth) {
        if (userBean.canLogin(auth.getUsername(), auth.getPassword())) {
            String token = issuer.issue(auth.getUsername());
            return Response.ok(token).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
   }


   @PATCH
   @Path("/set-password")
   @Authenticated
    public Response setPassword(AuthDTOpasswordSet authDTOpasswordSet) {
       // method 1
       String username = securityContext.getUserPrincipal().getName();

       // method 2
       /*
       public Response setPassword(@HeaderParam("Authorization") String authHeader, AuthDTOpasswordSet authDTOpasswordSet) {
       if (authHeader == null || !authHeader.startsWith("Bearer ")) {
           throw new NotAuthorizedException("Authorization header must be provided");
       }

       // Get token from the HTTP Authorization header
       String token = authHeader.substring("Bearer".length()).trim();

       var key = new SecretKeySpec(SECRET_KEY,ALGORITHM);
       String username = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
       */

       User user = userBean.findOrFail(username);

       if (userBean.canLogin(user.getUsername(), authDTOpasswordSet.getOldPassword())) {
           if (!(authDTOpasswordSet.getNewPassword().equals(authDTOpasswordSet.getConfirmPassword()))) {
               return Response.status(Response.Status.BAD_REQUEST).build();
           }

           if (userBean.setPassword(user.getUsername(), authDTOpasswordSet.getNewPassword())) {
               return Response.ok("Password changed with success!").build();
           } else {
               return Response.serverError().build();
           }
       }
       return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
