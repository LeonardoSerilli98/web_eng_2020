package authResources;

import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.security.Key;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author didattica
 */
@Provider
@Logged
@Priority(Priorities.AUTHENTICATION)
public class LoggedFilter implements ContainerRequestFilter {
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        String token = null;
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring("Bearer".length()).trim();
        }
        
        if (token != null && !token.isEmpty()) {
            
            String user = validateToken(requestContext,token);
            if(user!=null && !"".equals(user)){
                // per usare i parametri inseriti nel contesto in un metodo, basta inserire tra i 
                //parametri '@Context ContainerRequestContext crc ' e fare 'crc.getProperty"token/user")'
                requestContext.setProperty("token", token);
                requestContext.setProperty("user", user);                
            }else{
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }else{
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
       
        
        
    }

    private String validateToken(ContainerRequestContext requestContext, String token) {
        try{
            Key key = JWTHelpers.getInstance().getJwtKey();
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            System.out.println("#### valid token : " + token);
        }catch (Exception e) {
            System.out.println("#### invalid token : " + token);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return "";
        }
        
        
        //se il token è valido prendiamo l'utente associato al token dal db
        return "pippo"; 
    }

}
