package authResources;

import java.io.IOException;
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
        
        System.out.println("filtered");
        
        
    }

    private String validateToken(String token) {
//      //JWT                
//        Key key = AppGlobals.getInstance().getJwtKey();
//      Jws<Claims> jwsc = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

        return "pippo"; //andrebbe derivato dal token!
    }

}
