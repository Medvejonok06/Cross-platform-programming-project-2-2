package org.acme;

import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import org.acme.inventory.Inventory;      // Згенерований інтерфейс
import org.acme.inventory.StockRequest;   // Згенерований клас запиту
import org.acme.inventory.StockResponse;  // Згенерований клас відповіді

import java.util.Map;

@GrpcService // Ця анотація перетворює клас на gRPC сервіс
public class InventoryServiceImpl implements Inventory {

    // Імітація бази даних складу (ID товару -> Кількість)
    private final Map<Long, Integer> inventoryData = Map.of(
            1L, 100, // Elf Bar BC5000 - є багато
            2L, 5,   // Vaporesso XROS 3 - закінчується
            3L, 0    // Chaser 30ml - немає в наявності
    );

    @Override
    @Blocking // Дозволяємо простий синхронний код
    public Uni<StockResponse> checkStock(StockRequest request) {
        long id = request.getProductId();

        // Дістаємо кількість товару (або 0, якщо товару немає в мапі)
        int quantity = inventoryData.getOrDefault(id, 0);

        // Формуємо відповідь
        StockResponse response = StockResponse.newBuilder()
                .setProductId(id)
                .setQuantity(quantity)
                .setInStock(quantity > 0)
                .build();

        // Відправляємо результат
        return Uni.createFrom().item(response);
    }
}