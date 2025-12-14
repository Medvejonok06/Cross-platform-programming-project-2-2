package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.List;

@Path("/products") // Всі запити на адресу /products йтимуть сюди
public class ProductResource {

    // Створюємо "фейкову" базу даних з 3 товарів
    private final List<Product> products = List.of(
            new Product(1L, "Elf Bar BC5000", "Disposable", 350.00),
            new Product(2L, "Vaporesso XROS 3", "Pod System", 1100.00),
            new Product(3L, "Chaser 30ml", "Liquid", 250.00)
    );

    // Метод 1: Отримати всі товари
    @GET
    public List<Product> getAll() {
        return products;
    }

    // Метод 2: Отримати конкретний товар по ID
    @GET
    @Path("/{id}")
    public Product getById(@PathParam("id") Long id) {
        // Шукаємо товар у списку. Якщо не знайшли - повернеться null
        return products.stream()
                .filter(p -> p.id.equals(id))
                .findFirst()
                .orElse(null);
    }
}