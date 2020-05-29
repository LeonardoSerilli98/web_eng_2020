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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.Canale;
import models.Canale_Imp;

/**
 *
 * @author leonardo
 */
@Path("canali")
public class CanaliRes {
    
    
    @GET
    @Produces("application/json")
    public Response getAll(){
        //dobbiamo ritornare l'url di tutti i canali
        Canale c = new Canale_Imp();
        return Response.ok(c).build();
    }
    
    @Logged
    @POST
    @Consumes("application/json")
    public Response Store(@Context UriInfo uriinfo, Canale item ){
        return Response.ok("store un canale").build(); 
    }
    
    @Path("{id: [1-9]+}")
    public CanaleRes singleRes(){
        return new CanaleRes();
    }
    
    
}
