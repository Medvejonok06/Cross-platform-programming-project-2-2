package org.acme;

import io.quarkus.grpc.GrpcClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.acme.inventory.Inventory;      // gRPC інтерфейс
import org.acme.inventory.StockRequest;   // gRPC запит
import org.acme.inventory.StockResponse;  // gRPC відповідь
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/orders")
public class OrderResource {

    @Inject
    @RestClient
    ProductClient productClient; // Підключаємо REST клієнт

    @GrpcClient("inventory-service")
    Inventory inventoryService;  // Підключаємо gRPC клієнт

    @GET
    @Path("/{id}")
    public OrderDTO createOrder(@PathParam("id") Long productId) {
        OrderDTO order = new OrderDTO();

        // КРОК 1: Йдемо в Product Service (REST) за інформацією про товар
        // Це звичайний синхронний виклик
        Product product = productClient.getById(productId);

        // Якщо товару не існує
        if (product == null) {
            order.status = "PRODUCT NOT FOUND";
            return order;
        }

        order.productName = product.name;
        order.price = product.price;

        // КРОК 2: Йдемо в Inventory Service (gRPC) перевірити склад
        // build() створює запит
        StockRequest request = StockRequest.newBuilder().setProductId(productId).build();

        // await().indefinitely() - це спосіб перетворити асинхронний gRPC у синхронний
        // (ми чекаємо відповідь тут і зараз)
        StockResponse stockResponse = inventoryService
                .checkStock(request)
                .await().indefinitely();

        order.isAvailable = stockResponse.getInStock();

        // КРОК 3: Приймаємо рішення
        if (order.isAvailable) {
            order.status = "CONFIRMED - Order created for " + product.name;
        } else {
            order.status = "REJECTED - Out of stock";
        }

        return order;
    }
}