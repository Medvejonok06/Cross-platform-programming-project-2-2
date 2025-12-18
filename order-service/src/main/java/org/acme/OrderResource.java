package org.acme;

import io.quarkus.grpc.GrpcClient;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional; // Важливо для бази!
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.inventory.Inventory;
import org.acme.inventory.StockRequest;
import org.acme.inventory.StockResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    @RestClient
    ProductClient productClient;

    @GrpcClient("inventory-service")
    Inventory inventoryService;

    @Inject
    OrderRepository orderRepository; // <--- Впровадили репозиторій

    @POST // Змінили на POST, бо ми записуємо в базу
    @Path("/{id}")
    @Transactional // Обов'язково для збереження!
    public Order createOrder(@PathParam("id") Long productId) {
        // Створюємо нову сутність для бази
        Order order = new Order();
        order.setProductId(productId);

        // КРОК 1: REST запит до Product Service
        Product product = null;
        try {
            product = productClient.getById(productId);
        } catch (Exception e) {
            // Якщо товару немає або сервіс лежить
            order.setStatus("PRODUCT SERVICE UNAVAILABLE OR NOT FOUND");
            orderRepository.persist(order); // Зберігаємо навіть помилку
            return order;
        }

        order.setProductName(product.name);
        order.setPrice(product.price);

        // КРОК 2: gRPC запит до Inventory Service
        StockRequest request = StockRequest.newBuilder().setProductId(productId).build();
        StockResponse stockResponse = inventoryService
                .checkStock(request)
                .await().indefinitely();

        // КРОК 3: Приймаємо рішення і ЗБЕРІГАЄМО в базу
        if (stockResponse.getInStock()) {
            order.setStatus("CONFIRMED");
        } else {
            order.setStatus("REJECTED - Out of stock");
        }

        // <--- ГОЛОВНИЙ МОМЕНТ ЛАБОРАТОРНОЇ: Збереження через репозиторій
        orderRepository.persist(order);

        return order;
    }

    // Додатковий метод, щоб подивитися всі збережені замовлення
    @GET
    public java.util.List<Order> listAll() {
        return orderRepository.listAll();
    }
}