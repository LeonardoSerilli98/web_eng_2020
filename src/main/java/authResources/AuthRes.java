package authResources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.Base_Controller;
import daos.Utente_DAO_Imp;
import data.DataException;
import data.GuidaTV_DataLayer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.Utente;
import utilities.Database;
import utilities.MsgSerializer;

/**
 *
 * @author didattica /rest/auth
 *
 */
@Path("auth")
public class AuthRes {

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response doLogin(@Context UriInfo uriinfo, HashMap<String, String> input) {

        try {
            String email = input.get("email");
            String password = input.get("password");
            int auth = autenticazione(email, password);

            if (auth == 0) {

                String authToken = registraToken(uriinfo, email);

                Cookie cookie = new Cookie("jwt", authToken + ";max-age=900;HttpOnly=true;SameSite=Strict", "/", "localhost");

                String msg = MsgSerializer.serialize("logged succesfully");

                return Response.ok(msg)
                        .cookie(new NewCookie(cookie))
                        .build();

            } else if (auth == 1) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (IllegalArgumentException e) {

            return Response.status(Response.Status.UNAUTHORIZED).build();

        } catch (JsonProcessingException | DataException ex) {

            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

    }

    @Logged
    @DELETE
    @Path("/logout")
    public Response doLogout(@Context HttpServletRequest request) {
        try {

            javax.servlet.http.Cookie toCheck = null;
            for (javax.servlet.http.Cookie c : request.getCookies()) {
                if (c.getName().equals("jwt")) {
                    toCheck = c;
                }
            }

            revokeToken(toCheck);
            Cookie cookie = new Cookie("jwt", "expired;max-age=0;HttpOnly=true;SameSite=Strict", "/", "localhost");

            return Response.ok("logout succed")
                    .cookie(new NewCookie(cookie))
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/refresh")
    public Response refreshToken(ContainerRequestContext requestContext) {

        String token = null;
        Utente u = null;

        if (requestContext.getCookies().containsKey("jwt")) {
            token = requestContext.getCookies().get("jwt").getValue();
        }

        if (token != null && !token.isEmpty()) {
            try {
                Key key = JWTHelpers.getInstance().getJwtKey();
                Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
                u = Database.getDatalayer().getUtenteDAO().getUtenteByToken(token);
                if (u == null) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
            } catch (ExpiredJwtException e) {
                System.out.println("#### expired token: " + token);
            } catch (MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException e) {
                System.out.println("#### invalid token: " + token);
                return Response.status(Response.Status.UNAUTHORIZED).build();
            } catch (DataException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }

        try {
            Cookie cookie = new Cookie("jwt", "expired;max-age=900;HttpOnly=true;SameSite=Strict", "/", "localhost");
            NewCookie newCookie = new NewCookie(cookie);

            return Response.ok(MsgSerializer.serialize("refresh succed"))
                    .cookie(newCookie)
                    .build();

        } catch (IllegalArgumentException | JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    private int autenticazione(String username, String password) {
        try {

            Utente u = (Database.getDatalayer()).getUtenteDAO().checkUtente(username, password);
            if (u != null) {
                return 0;
            }
        } catch (DataException ex) {
            Logger.getLogger(AuthRes.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

        return 1;
    }

    private String registraToken(UriInfo context, String username) throws DataException {
        Key key = JWTHelpers.getInstance().getJwtKey();
        Date date = Date.from(LocalDateTime.now().plusMinutes(180L).atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuer(context.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .signWith(key)
                .setExpiration(date)
                .compact();
        Utente u = Database.getDatalayer().getUtenteDAO().getUtenteByUsername(username);
        u.setToken(token);
        Database.getDatalayer().getUtenteDAO().update(u);

        return token;
    }

    private void revokeToken(javax.servlet.http.Cookie cookie) {
        /* invalidate il token per il logout sul db*/
    }

}
