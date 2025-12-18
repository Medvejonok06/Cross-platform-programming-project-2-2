package org.acme;

import jakarta.transaction.Transactional; // Важливо для запису в БД
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    // @GET: Отримати всі товари з бази
    @GET
    public List<Product> list() {
        return Product.listAll(); // Магія Active Record
    }

    // @GET: Отримати один товар
    @GET
    @Path("/{id}")
    public Product get(@PathParam("id") Long id) {
        return Product.findById(id);
    }

    // @POST: Створити товар (Потребує транзакції)
    @POST
    @Transactional
    public Response create(Product product) {
        product.persist(); // Зберегти в базу
        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    // @DELETE: Видалити товар
    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Product.deleteById(id);
    }
}