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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import models.Palinsesto;
import models.Ricerca;
import models.Ricerca_Imp;
import utilities.Database;

/**
 *
 * @author leonardo
 */
@Path("palinsesti")
public class PalinsestiRes {

    @GET
    @Produces("application/json")
    public Response ricercaPalinsesti(
            @QueryParam("canale") String nomeCanale,
            @QueryParam("titolo") String titolo,
            @QueryParam("data") String data,
            @QueryParam("fascia") String fascia,
            @QueryParam("genere") String genereNome
    ) {

        List<Palinsesto> palinsesti = null;

        try {
            Ricerca r = new Ricerca_Imp();

            if (data == null || data == "") {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String today = sdf.format(java.util.Date.from(LocalDateTime.now().minusDays(3).atZone(ZoneId.systemDefault()).toInstant()));
                r.setData(Date.valueOf(today));
            }

            r.setGenere(Database.getDatalayer().getGenereDAO().getGenereByName(genereNome));
            r.setProgramma(Database.getDatalayer().getProgrammaDAO().checkExistence(titolo));
            r.setFascia(Database.getDatalayer().getFasciaDAO().getFasciaByName(fascia));
            r.setGenere(Database.getDatalayer().getGenereDAO().getGenereByName(genereNome));

            palinsesti = Database.getDatalayer().getPalinsestoDAO().ricerca(r);
            
            if (palinsesti == null) {
                return Response.noContent().status(Response.Status.NOT_FOUND).build();
            }
            
            //System.out.println(r.getData(), r.get);
            return Response.ok(palinsesti).build();

        } catch (DataException ex) {
                return Response.noContent().status(Response.Status.BAD_REQUEST).build();
        }


    }

}
