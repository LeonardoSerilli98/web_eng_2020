package authResources;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
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
        
        if (requestContext.getCookies().containsKey("jwt")) {
            token = requestContext.getCookies().get("jwt").getValue();
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
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();                                                                                                                                                                                                                   
            System.out.println("#### valid token: " + token);
        }catch (Exception e) {
            System.out.println("#### invalid token: " + token);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return "";
        }    
        
        //controllo se coincide con quello sul db
        //se il token è valido prendiamo l'utente associato al token dal db
        return "pippo";
    }
}
