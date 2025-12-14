package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

// 1. Описуємо, як виглядає товар, який ми отримаємо з іншого сервісу
class Product {
    public Long id;
    public String name;
    public double price;
}

// 2. Описуємо сам клієнт
@RegisterRestClient(configKey = "product-api") // Ця назва має збігатися з application.properties
@Path("/products")
public interface ProductClient {

    @GET
    @Path("/{id}")
    Product getById(@PathParam("id") Long id);
}