/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import authResources.Logged;
import com.fasterxml.jackson.core.JsonProcessingException;
import data.DataException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.Canale;
import models.Immagine;
import utilities.Database;
import utilities.MsgSerializer;

/**
 *
 * @author leonardo
 */
@Path("canali")
public class CanaliRes {

    @GET
    @Produces("application/json")
    public Response getAll(
            @Context UriInfo uriinfo) {

        List<Map<String, Object>> l = new ArrayList();

        try {
            List<Canale> canali = (Database.getDatalayer()).getCanaleDAO().getAll();
            for (Canale c : canali) {
                Map<String, Object> e = new HashMap<>();

                URI uri = uriinfo.getBaseUriBuilder()
                        .path(getClass())
                        .path(c.getKey().toString())
                        .build();

                e.put("canale", c);
                e.put("url", uri.toString());
                l.add(e);
            }

        } catch (DataException ex) {
            Response.noContent().status(Response.Status.BAD_REQUEST).build();
        }

        if (l.isEmpty()) {
            Response.noContent().status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(l).build();
    }

    @Logged
    @POST
    @Consumes("application/json")
    public Response Store(@Context UriInfo uriinfo, Canale item) {

        try {
            Canale existC = Database.getDatalayer().getCanaleDAO().checkExistence(item.getNome());
            Immagine existI = Database.getDatalayer().getImmagineDAO().checkExistence(item.getImmagine().getNome());

            if (existC != null) {
                return Response.noContent().status(Response.Status.CONFLICT).build();
            }

            if (existI == null) {
                Database.getDatalayer().getImmagineDAO().create(item.getImmagine());
                existI = Database.getDatalayer().getImmagineDAO().checkExistence(item.getImmagine().getNome());
            }

            item.setImmagine(existI);
            Database.getDatalayer().getCanaleDAO().create(item);

        } catch (DataException ex) {

            Response.noContent().status(Response.Status.BAD_REQUEST).build();

        }

        return Response.noContent().status(Response.Status.CREATED).build();
    }

    @Path("{id: [0-9]+}")
    public CanaleRes singleRes() {
        return new CanaleRes();
    }

}
