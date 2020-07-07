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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.Canale_Imp;
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

    //non funziona senza data e/o senza canale
    @GET
    @Produces("application/json")
    public Response ricercaPalinsesti(            
            @Context UriInfo uriinfo,
            @QueryParam("canale") String canaleNome,
            @QueryParam("titolo") String titolo,
            @QueryParam("data") String data,
            @QueryParam("fascia") String fascia,
            @QueryParam("genere") String genereNome,
            @QueryParam("pagina") int pagina
    ) {

        List<Palinsesto> palinsesti = null;
        HashMap<String, Object> results = new HashMap();

        try {
            Ricerca r = new Ricerca_Imp();

            if (data == null || data == "") {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String today = sdf.format(java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                System.out.println(Date.valueOf(today));
                r.setData(Date.valueOf(today));
            }

            if (genereNome != null || genereNome != "") {
                if (Database.getDatalayer().getGenereDAO().getGenereByName(genereNome) != null) {
                    r.setGenere(Database.getDatalayer().getGenereDAO().getGenereByName(genereNome));
                }
            }
            if (titolo != null || titolo != "") {
                if (Database.getDatalayer().getProgrammaDAO().checkExistence(titolo) != null) {
                    r.setProgramma(Database.getDatalayer().getProgrammaDAO().checkExistence(titolo));
                }
            }
            if (fascia != null || fascia != "") {
                if (Database.getDatalayer().getFasciaDAO().getFasciaByName(fascia) != null) {
                    r.setFascia(Database.getDatalayer().getFasciaDAO().getFasciaByName(fascia));
                }
            }
            if (canaleNome != null || canaleNome != "") {
                if (Database.getDatalayer().getCanaleDAO().checkExistence(canaleNome) != null) {
                    r.setCanale(Database.getDatalayer().getCanaleDAO().checkExistence(canaleNome));
                }
            }
            
            if(pagina==0){
                palinsesti = Database.getDatalayer().getPalinsestoDAO().ricerca(r, 0);
                
            }else{
                palinsesti = Database.getDatalayer().getPalinsestoDAO().ricerca(r, pagina);
            }

            if (palinsesti == null) {
                return Response.noContent().status(Response.Status.NOT_FOUND).build();
            }
            
            String pagePrec = uriinfo.getBaseUriBuilder().path(getClass()).build().toString() +
                    "?canale=" + canaleNome + 
                    "&titolo=" + titolo + 
                    "&genere=" + genereNome + 
                    "&data=" + data + 
                    "&fascia=" + fascia +
                    "&pagina=" + pagina;
            String pageSucc = uriinfo.getBaseUriBuilder().path(getClass()).build().toString() +
                    "?canale=" + canaleNome + 
                    "&titolo=" + titolo + 
                    "&genere=" + genereNome + 
                    "&data=" + data + 
                    "&fascia=" + fascia +
                    "&pagina=" + (pagina+5);
            if(pagina!=0){
                results.put("pagina precedente", pagePrec);
            }
            results.put("pagina successiva", pageSucc);
            results.put("palinsesti", palinsesti);
            return Response.ok(results).build();

        } catch (DataException ex) {
            return Response.noContent().status(Response.Status.BAD_REQUEST).build();
        }

    }

}
