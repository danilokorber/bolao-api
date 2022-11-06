package io.easyware.boundary;

import io.quarkus.security.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("")
public class Test {

    @Context SecurityContext sec;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello() {
        return Response.ok().entity("Hello World").build();
    }

    @GET
    @Path("/secure")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response secured() {
        System.out.println(sec.getUserPrincipal().getName());
        return Response.ok().entity(sec.getUserPrincipal()).build();
    }

    @GET
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("admin")
    public Response admin() {
        return Response.ok().build();
    }

    @GET
    @Path("/notify")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("user")
    public Response user() {
        return Response.ok().build();
    }

}
