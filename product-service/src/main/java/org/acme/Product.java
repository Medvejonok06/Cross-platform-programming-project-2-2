package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity; // 1. Батьківський клас Active Record
import jakarta.persistence.Entity; // 2. Анотація JPA

@Entity // Це каже Quarkus створити таблицю "Product"
public class Product extends PanacheEntity {

    // Поле id вже є в PanacheEntity, тому його писати не треба!

    public String name;
    public String category;
    public double price;

    // Конструктор
    public Product() {}

    public Product(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }
}