/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import models.Episodio;
import models.Episodio_Imp;

/**
 *
 * @author leonardo
 */
public class EpisodiRes {
    
    public EpisodiRes(){
        Episodio item = new Episodio_Imp();       
    }
    
    @GET
    @Produces("application/json")
    public Response episodiByProgramma(){
 
        return Response.ok("ritorna le URL di tutti i singoli episodi di un programma").build();
    }
    
    
    
}
