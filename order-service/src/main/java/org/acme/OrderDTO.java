package org.acme;

public class OrderDTO {
    public String productName;
    public double price;
    public String status;      // Наприклад: "CONFIRMED" або "REJECTED"
    public boolean isAvailable;

    // Порожній конструктор
    public OrderDTO() {}
}