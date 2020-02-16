package com.arthurgrig.vendingMachine;

public enum Coin {

    PENNY(1), NICKEL(5), DIME(10), QUARTER(25);

    private final int value;

    private Coin(int v) {
        this.value = v;
    }

    public int getValue() {
        return value;
    }
}
