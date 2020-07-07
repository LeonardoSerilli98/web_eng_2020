
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import authResources.Logged;
import data.DataException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.Canale;
import models.Canale_Imp;
import models.Immagine;
import utilities.Database;
import utilities.MsgSerializer;
import utilities.SecurityLayer;

/**
 *
 * @author leonardo
 */
public class CanaleRes {

    public CanaleRes() {
        Canale item = new Canale_Imp();
    }

    @GET
    @Produces("application/json")
    public Response getSingleCanale(@Context UriInfo uriinfo, @PathParam("id") String idCanale) {

        int id = SecurityLayer.checkNumeric(idCanale);
        Canale c = null;
        try {
            c = Database.getDatalayer().getCanaleDAO().read(id);
            if (c == null) {
                return Response.noContent().status(Response.Status.NOT_FOUND).build();

            }
        } catch (DataException ex) {

            Response.noContent().status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(c).build();
    }

    @Logged
    @PUT
    @Consumes("application/json")
    public Response Update(@Context UriInfo uriinfo,
            Canale item,
            @PathParam("id") String idCanale,
            @Context ContainerRequestContext crc) {

        String username = crc.getProperty("user").toString();
        try {
            if (!(Database.getDatalayer().getUtenteDAO().isAdmin(username))) {

                return Response.noContent().status(Response.Status.UNAUTHORIZED).build();

            }
        } catch (DataException ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        int id = SecurityLayer.checkNumeric(idCanale);

        try {
            Canale existC = Database.getDatalayer().getCanaleDAO().read(id);
            Immagine existI = Database.getDatalayer().getImmagineDAO().checkExistence(item.getImmagine().getNome());

            if (existC == null) {
                return Response.noContent().status(Response.Status.NOT_FOUND).build();
            }

            if (existI == null) {
                Database.getDatalayer().getImmagineDAO().create(item.getImmagine());
                existI = Database.getDatalayer().getImmagineDAO().checkExistence(item.getImmagine().getNome());
            }

            item.setKey(existC.getKey());
            item.setImmagine(existI);
            item.setVersion(existC.getVersion());
            Database.getDatalayer().getCanaleDAO().update(item);

        } catch (DataException ex) {
            Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        String uri = uriinfo.getBaseUriBuilder().build().toString();

        return Response.ok(MsgSerializer.serialize(item.getNome(), uri + "canali/" + id)).status(Response.Status.CREATED).build();

    }

    @Path("palinsesto")
    public PalinsestoRes getPalinsesto() {
        return new PalinsestoRes();
    }

}
