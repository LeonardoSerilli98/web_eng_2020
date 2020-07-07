/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import authResources.Logged;
import data.DataException;
import freemarker.template.utility.NullArgumentException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.Programma;
import models.Programma_Imp;
import utilities.Database;
import utilities.MsgSerializer;
import utilities.SecurityLayer;

/**
 *
 * @author leonardo
 */
public class ProgrammaRes {

    public ProgrammaRes() {
        Programma item = new Programma_Imp();
    }

    @GET
    @Produces("application/json")
    public Response getSingleProgramma(@PathParam("id") String idProgramma) {

        int id = SecurityLayer.checkNumeric(idProgramma);
        Programma p = null;
        try {
            p = Database.getDatalayer().getProgrammaDAO().read(id);
            if (p == null) {
                return Response.noContent().status(Response.Status.NOT_FOUND).build();

            }
        } catch (DataException ex) {

            Response.noContent().status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(p).build();
    }

    @Logged
    @PUT
    @Consumes("application/json")
    public Response Update(@Context UriInfo uriinfo,
            Programma item,
            @PathParam("id") String idProgramma,
            @Context ContainerRequestContext crc) {

        String username = crc.getProperty("user").toString();
        try {
            if (!(Database.getDatalayer().getUtenteDAO().isAdmin(username))) {

                return Response.noContent().status(Response.Status.UNAUTHORIZED).build();

            }
        } catch (DataException ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        int id = 0;

        try {
            if (item != null) {

                Programma p = Database.getDatalayer().getProgrammaDAO().read(SecurityLayer.checkNumeric(idProgramma));
                if (p != null) {
                    id = (p.getKey());
                    item.setVersion(p.getVersion());
                    item.setKey(p.getKey());
                    Database.getDatalayer().getProgrammaDAO().update(item);
                } else {
                    return Response.noContent().status(Response.Status.BAD_REQUEST).build();
                }

            } else {
                return Response.noContent().status(Response.Status.BAD_REQUEST).build();
            }
        } catch (DataException ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        String uri = uriinfo.getBaseUriBuilder().build().toString();

        return Response.ok(MsgSerializer.serialize(item.getNome(), uri + "programmi/" + id)).status(Response.Status.CREATED).build();

    }

    @Path("episodi")
    public EpisodiRes getEpisodi() {
        return new EpisodiRes();
    }
}
