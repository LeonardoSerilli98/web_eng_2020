package authResources;

import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author didattica /rest/auth
 *
 */
@Path("auth")
public class AuthRes {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response doLogin(@Context UriInfo uriinfo,            
            @FormParam("email") String email,
            @FormParam("password") String password ) {
        try {
            if (autenticazione(email, password)) {   
                
                String authToken = registraToken(uriinfo, email);                
             
                return Response.ok(authToken)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .build();
                
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Logged
    @DELETE
    @Path("/logout")
    public Response doLogout(@Context HttpServletRequest request) {
        try {
            //estraiamo i dati inseriti dal nostro LoggedFilter...
            String token = (String) request.getAttribute("token");
            if (token != null) {
                revokeToken(token);
            }
            return Response.ok("slogged").build();
            //return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    private boolean autenticazione(String username, String password) {
        /* query e check username/password */
        return true;
    }

    private String registraToken(UriInfo context, String username) {
       
    //JWT        
        Key key = JWTHelpers.getInstance().getJwtKey();
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuer(context.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(15L).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key)
                .compact();
        
        //prima di ridarlo all'utente dove lo salviamo?
        
        return token;
    }

    private void revokeToken(String token) {
        /* invalidate il token per il logout*/
    }
}
