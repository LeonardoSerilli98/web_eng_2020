/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import authResources.Logged;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.Programma;

/**
 *
 * @author leonardo
 */
@Path("programmi")
public class ProgrammiRes {
    
    @Path("{id: [1-9]+}")
    public ProgrammaRes singleRes(){
        return new ProgrammaRes();
    }
    
    @Logged
    @POST
    @Consumes("application/json")
    public Response Store(@Context UriInfo uriinfo, Programma item){
        
        return Response.ok("store un programma").build();
      
    }
    
    
    
}
