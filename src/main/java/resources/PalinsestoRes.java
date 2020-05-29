/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import models.Palinsesto;
import models.Palinsesto_Imp;

/**
 *
 * @author leonardo
 */
public class PalinsestoRes {
    
    public PalinsestoRes(){
        Palinsesto item = new Palinsesto_Imp();       
    }
    
    @GET
    @Produces("application/json")
    public Response getPalinsestoByCanale(
        @QueryParam("data") String data){
        
        if(data!=null && data!=""){
            
             return Response.ok("singolo palinsesto by canale and data").build();
            
        }
        return Response.ok("singolo palinsesto by canale").build();
      
    }
    
}
