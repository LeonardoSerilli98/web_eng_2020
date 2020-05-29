/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.xml.crypto.Data;
import models.Palinsesto;
import models.Palinsesto_Imp;

/**
 *
 * @author leonardo
 */
@Path("palinsesti")
public class PalinsestiRes {
        
    @GET
    @Produces("application/json")
    public Response ricercaPalinsesti(
        @QueryParam("data") String data,
        @QueryParam("canale") String canale,
        @QueryParam("titolo") String titolo,
        @QueryParam("genere") String genere,
        @QueryParam("isSerie") String isSerie,
        @QueryParam("fascia") String fascia){
        
        boolean noParams = true;
        boolean onlyByData = false;

        
        //filtriamo i risultati in base ai campi della ricerca
        if(data!=null && data!=""){
            noParams=false;
            onlyByData = true;
        }
        if(canale!=null && canale!=""){
            noParams=false;     
            onlyByData = false;
        }
        if(titolo!=null && titolo!=""){
            noParams=false;
            onlyByData = false;
        }
        if(genere!=null && genere!=""){
            noParams=false;
            onlyByData = false;
        }
        if(isSerie=="true"){
            noParams=false;    
            onlyByData = false;
        }
        if(fascia!=null && fascia!=""){
            noParams=false;    
            onlyByData = false;
        }
        
        // se nessun parametro è stato specificato tornaiamo i palinsesti di oggi
        if(noParams){
            return Response.ok("ritorna il palinsesto odierno di tutti i canali").build(); 
        }else if(onlyByData){
            return Response.ok("ritorna il palinsesto odierno di tutti i canali per la data indicata").build(); 
        }
        
        return Response.ok("ritorna il palinsestoi in base ai campi della ricerca").build();

    }
        
}
