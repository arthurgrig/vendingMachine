package com.arthurgrig.vendingMachine;

public class ShoppingCart {

    /**
     * Only one product can be in shopping cart
     */
    private Product product;

    /**
     * Sets latest selection as the item in shopping cart
     *
     * @param p
     */
    public void selectProduct(Product p) {
        product = p;
    }

    public void reset() {
        product = null;
    }

    public Product getProduct() {
        return product;
    }

    /**
     * Confirms purchase and empties shopping cart
     *
     * @return returns product to be given to customer
     */
    public Product checkOut() {
        Product p = product;
        product = null;
        return p;
    }

}
