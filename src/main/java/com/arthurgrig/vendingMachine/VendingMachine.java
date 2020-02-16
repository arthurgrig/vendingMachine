package com.arthurgrig.vendingMachine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VendingMachine {

    private static final String NO_MORE_PRODUCT_ERROR = "No More %s left";
    private static final String CANT_ACCEPT_MORE_COINS_ERROR = "Can't take more %s coins";
    private static final String NOT_ENOUGH_CHANGE = "Not enough change for %s";

    private static final String COIN_ADDED = "Coin added %s";
    private static final String VENDING_PRODUCT = "Vending product %s, funds remaining %s";


    // max number of products for every product
    private static final int PRODUCT_MAX_SIZE = 20;

    // max number of coins for every coin type (this can be set individually later)
    private static final int COINS_MAX_SIZE = 1000;

    // VM state
    // how many of each product machine contains
    private final Map<Product, Integer> productsQuantity = new HashMap<>();
    // max size for each product

    // how many of each coin machine contains
    private final Map<Coin, Integer> coinsQuantity = new HashMap<>();


    private User user;

    public VendingMachine() {
        user = new User();
    }

    // Supplier api

    /**
     * Adds product to vending machine
     *
     * @param p        product type
     * @param quantity quantity of how many to add
     * @return effectively how many was added (quantity may not fit into the machine)
     */
    public int addProduct(Product p, int quantity) throws ProductException {
        Integer q = productsQuantity.getOrDefault(p, 0);

        q = q + quantity;

        if (q > PRODUCT_MAX_SIZE) {
            throw new ProductException("Can't add more prdocuts of type: " + p.name());
        } else {
            productsQuantity.put(p, q);
            return quantity;
        }
    }

    public int removeProduct(Product p) throws ProductException {
        int q = productsQuantity.getOrDefault(p, 0) - 1;

        if (q < 0) {
            throw new ProductException("No more products of type: " + p.name());
        } else {
            productsQuantity.put(p, q);
            return q;
        }
    }

    public int addCoins(Coin c, int quantity) throws CoinException {
        int q = coinsQuantity.getOrDefault(c, 0) + quantity;

        if (q > COINS_MAX_SIZE) {
            throw new CoinException("Can't add more coins of type: " + c);
        } else {
            coinsQuantity.put(c, q);
            return quantity;
        }
    }


    // Customer
    //

    /**
     * Transaction starts when user puts first coin
     *
     * @param c Coin
     */
    public int addCoin(Coin c) {
        if (user == null) {
            user = new User();
        }

        try {
            addCoins(c, 1);
        } catch (CoinException ce) {
            System.out.println(String.format(CANT_ACCEPT_MORE_COINS_ERROR, c.name()));
        }

        user.addMoney(c);

        System.out.println(String.format(COIN_ADDED, c));

        return user.getMoneyDeposited();
    }

    public void selectProduct(Product p) {
        user.selectProduct(p);
    }

    /**
     * @return Optional<Product> if possible
     */
    public Optional<Product> checkout() {
        Product p = user.getProduct();

        int changeTarget = user.getMoneyDeposited() - p.getPrice();

        Map<Coin, Integer> change = CoinChangeHelper.coinChange(coinsQuantity, changeTarget);

        // no change is possible
        if (change == null) {
            System.out.println(String.format(NOT_ENOUGH_CHANGE, changeTarget));
            return Optional.empty();
        }

        try {
            removeProduct(user.getProduct());
        } catch (ProductException pe) {
            System.out.println(String.format(NO_MORE_PRODUCT_ERROR, p.name()));

            return Optional.empty();
        }

        // reset the user
        int balance = user.checkOut();
        deductFunds(change, balance);

        System.out.println(String.format(VENDING_PRODUCT, p.name(), balance));


        return Optional.of(p);
    }


    /**
     * User cancels and maybe get refund
     *
     * @return Refund in coins
     */
    public Map<Coin, Integer> cancel() {
        Map<Coin, Integer> change = CoinChangeHelper.coinChange(coinsQuantity, user.getMoneyDeposited());
        int refund = user.reset();
        deductFunds(change, refund);

        return change;
    }

    private void deductFunds(Map<Coin, Integer> change, int deduct) {
        // deduct each coin from vending machines coins
        change.forEach((key, value) -> coinsQuantity.put(key, deduct));
    }

    public Map<Coin, Integer> getCoinsQuantity() {
        return Collections.unmodifiableMap(coinsQuantity);
    }

    public int getProductsQuantity(Product p) {
        return productsQuantity.getOrDefault(p, 0);
    }

}
