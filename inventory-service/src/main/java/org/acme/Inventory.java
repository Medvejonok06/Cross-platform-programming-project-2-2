package org.acme;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "inventory") // Вказуємо ім'я колекції (таблиці)
public class Inventory extends PanacheMongoEntity {

    public Long productId;
    public int quantity;

    public Inventory() {}

    public Inventory(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Метод для пошуку по productId
    public static Inventory findByProductId(Long productId) {
        return find("productId", productId).firstResult();
    }
}