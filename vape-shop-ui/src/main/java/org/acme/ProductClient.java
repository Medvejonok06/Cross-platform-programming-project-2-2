package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

// Простий клас для даних товару (DTO)
class Product {
    public Long id;
    public String name;
    public String category;
    public double price;
}

// Вказуємо configKey, який ми прописали в application.properties
@RegisterRestClient(configKey = "product-api")
@Path("/products") // Цей шлях додається до URL (http://localhost:7071/products)
public interface ProductClient {

    @GET
    List<Product> getAll();
}