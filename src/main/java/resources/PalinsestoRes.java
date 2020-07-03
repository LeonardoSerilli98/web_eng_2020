/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import data.DataException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.Canale;
import models.Palinsesto;
import models.Palinsesto_Imp;
import utilities.Database;
import utilities.SecurityLayer;

/**
 *
 * @author leonardo
 */
public class PalinsestoRes {

    public PalinsestoRes() {
        Palinsesto item = new Palinsesto_Imp();
    }

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    public Response getPalinsestoByCanale(@Context UriInfo uriinfo, @QueryParam("data") String data, @PathParam("id") String idCanale) {
        int id = SecurityLayer.checkNumeric(idCanale);
        
        String dataToSearch = null;
        List<Palinsesto> palinsesti = null;

        if (data != null && data != "") {

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String todayMinus3 = sdf.format(java.util.Date.from(LocalDateTime.now().minusDays(3).atZone(ZoneId.systemDefault()).toInstant()));
            String todayPlus7 = sdf.format(java.util.Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant()));

            if (Date.valueOf(data).before(Date.valueOf(todayMinus3)) || Date.valueOf(data).after(Date.valueOf(todayPlus7))) {
                return Response.noContent().status(Response.Status.FORBIDDEN).build();
            }

            dataToSearch = (String) data;

        }
        

        try {
            Canale existC = Database.getDatalayer().getCanaleDAO().read(id);

            if (existC == null) {

                Response.noContent().status(Response.Status.NOT_FOUND).build();
            }

            if(dataToSearch!=null){
                 palinsesti = Database.getDatalayer().getPalinsestoDAO().getPalinsestiByCanale(existC, dataToSearch);
            }else{
                 palinsesti = Database.getDatalayer().getPalinsestoDAO().getPalinsestiByCanale(existC);
            }
            
            if(palinsesti == null){
                
                Response.noContent().status(Response.Status.NOT_FOUND).build();
            }

        } catch (DataException ex) {
            Response.noContent().status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok(palinsesti).build();

    }

}
