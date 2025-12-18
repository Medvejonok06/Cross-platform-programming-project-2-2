package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/inventory")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InventoryResource {

    @GET
    public List<Inventory> list() {
        return Inventory.listAll();
    }

    @GET
    @Path("/{productId}")
    public Inventory get(@PathParam("productId") Long productId) {
        return Inventory.findByProductId(productId);
    }

    // Приклад додавання (для тесту)
    @POST
    public void add(Inventory inventory) {
        inventory.persist();
    }
}