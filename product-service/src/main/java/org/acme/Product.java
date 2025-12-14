package org.acme;

public class Product {
    public Long id;          // Унікальний номер
    public String name;      // Назва (наприклад, "Elf Bar")
    public String category;  // Категорія ("Pod System", "Liquid")
    public double price;     // Ціна

    // Порожній конструктор потрібен для роботи Quarkus/JSON
    public Product() {}

    // Конструктор для швидкого створення товарів у коді
    public Product(Long id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }
}