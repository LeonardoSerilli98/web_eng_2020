/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import authResources.Logged;
import data.DataException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.Programma;
import utilities.Database;
import utilities.MsgSerializer;

/**
 *
 * @author leonardo
 */
@Path("programmi")
public class ProgrammiRes {

    @Path("{id: [0-9]+}")
    public ProgrammaRes singleRes() {
        return new ProgrammaRes();
    }

    @Logged
    @POST
    @Consumes("application/json")
    public Response Store(@Context UriInfo uriinfo,
            Programma item,
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

                Programma existProg = Database.getDatalayer().getProgrammaDAO().checkExistence(item.getNome());
                if (existProg == null) {
                    Database.getDatalayer().getProgrammaDAO().create(item);
                } else {
                    return Response.noContent().status(Response.Status.BAD_REQUEST).build();
                }

                id = Database.getDatalayer().getProgrammaDAO().checkExistence(item.getNome()).getKey();

            } else {
                return Response.noContent().status(Response.Status.BAD_REQUEST).build();
            }
        } catch (DataException ex) {

            return Response.noContent().status(Response.Status.BAD_REQUEST).build();
        }
        String uri = uriinfo.getBaseUriBuilder().path(getClass()).build().toString();
        return Response.ok(MsgSerializer.serialize(item.getNome(), uri + "/" + id)).status(Response.Status.CREATED).build();

    }

}
