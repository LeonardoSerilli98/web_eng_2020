/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import authResources.Logged;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.Programma;
import models.Programma_Imp;

/**
 *
 * @author leonardo
 */
public class ProgrammaRes {
    
    public ProgrammaRes(){
        Programma item = new Programma_Imp();       
    }
    
    @GET
    @Produces("application/json")
    public Response getSingleProgramma(){
        return Response.ok("ritorna un singolo programma").build();
    }
    
    @Logged
    @PUT
    @Consumes("application/json")
    public Response Update(@Context UriInfo uriinfo, Programma item ){
        
        return Response.ok("update di un Programma").build();
      
    }
    
    @Path("episodi")
    public EpisodiRes getEpisodi(){
        return new EpisodiRes();
    }
}
