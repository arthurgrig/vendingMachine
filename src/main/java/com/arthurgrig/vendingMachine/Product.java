package com.arthurgrig.vendingMachine;

public enum Product {

    COKE(25), PEPSI(35), SODA(45);

    private int price;

    private Product(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
