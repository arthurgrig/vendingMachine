package com.arthurgrig.vendingMachine;

public class User {

    private final ShoppingCart shoppingCart;
    private int moneyDeposited = 0;


    public User() {
        shoppingCart = new ShoppingCart();
    }

    public void addMoney(Coin c) {
        moneyDeposited += c.getValue();
    }

    public int getMoneyDeposited() {
        return moneyDeposited;
    }

    public void selectProduct(Product p) {
        shoppingCart.selectProduct(p);
    }

    /**
     * @return returns change needed
     */
    public int checkOut() {
        Product p = shoppingCart.checkOut();

        moneyDeposited -= p.getPrice();

        if (moneyDeposited < 0) {
            throw new IllegalStateException("Impossible! Not enough funds");
        }

        return moneyDeposited;
    }

    public Product getProduct() {
        return shoppingCart.getProduct();
    }

    /**
     * reset user's funds and shopping cart
     *
     * @return
     */
    public int reset() {
        int refund = moneyDeposited;
        shoppingCart.reset();
        moneyDeposited = 0;
        return refund;
    }

}
