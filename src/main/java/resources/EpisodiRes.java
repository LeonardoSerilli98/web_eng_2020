/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import data.DataException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.Canale;
import models.Episodio;
import models.Episodio_Imp;
import models.Programma;
import utilities.Database;
import utilities.SecurityLayer;

/**
 *
 * @author leonardo
 */
public class EpisodiRes {

    public EpisodiRes() {
        Episodio item = new Episodio_Imp();
    }

    @GET
    @Produces("application/json")
    public Response episodiByProgramma(@PathParam("id") String idProg,
            @Context UriInfo uriinfo) {

        List<Episodio> episodi = null;
        List<Map<String, Object>> l = new ArrayList();

        try {
            Programma p = Database.getDatalayer().getProgrammaDAO().read(SecurityLayer.checkNumeric(idProg));
            if (p == null) {
                return Response.noContent().status(Response.Status.NOT_FOUND).build();
            }
            episodi = Database.getDatalayer().getEpisodioDAO().getEpisodiByProgramma(p);
            if (episodi == null) {
                return Response.noContent().status(Response.Status.NOT_FOUND).build();
            }
            for (Episodio episodio : episodi) {
                Map<String, Object> e = new HashMap<>();

                String uri = uriinfo.getBaseUriBuilder().build().toString()+"/episodi/"+episodio.getKey();

                //e.put("episodio", episodio);
                e.put("url", uri);
                l.add(e);
            }
        } catch (DataException ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(l).build();

    }

}
