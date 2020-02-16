package com.arthurgrig.vendingMachine


import spock.lang.Specification
import spock.lang.Unroll

import static com.arthurgrig.vendingMachine.Coin.*
import static com.arthurgrig.vendingMachine.Product.*

class VendingMachineTest extends Specification {

    VendingMachine vm = new VendingMachine()

    def setup() {
        // Products are added to the machine
        vm.addProduct(COKE, 10)
        vm.addProduct(PEPSI, 10)
        vm.addProduct(SODA, 10)
        // Coins are added to the machine
        vm.addCoins(PENNY, 900)
        vm.addCoins(NICKEL, 500)
        vm.addCoins(DIME, 200)
        vm.addCoins(QUARTER, 100)
    }

    @Unroll
    def "Run machine  select #product, insert #paid "() {
        given: "A Vending machine"
        vm
        int initialProductQuantity = vm.getProductsQuantity(product)

        and: "user adds coins"
        paid.each { vm.addCoin(it) }

        when: "User selects a product"
        vm.selectProduct(product)

        and: "User checks out"
        Optional<Product> result = vm.checkout()

        then: "product is removed from the stock"
        if (result.isPresent()) {
            vm.getProductsQuantity(product) == initialProductQuantity - 1
        }

        result.orElse(null) == expected

        and: "withdrawn change is returned to the user if owed"
        Map<Coin, Integer> refund = vm.cancel()
        expectedCoins == refund

        where:
        product | paid                                            | expected | expectedCoins
        COKE    | [DIME, DIME, DIME]                              | COKE     | [(NICKEL): 1] // vending coke
        COKE    | [DIME, DIME, PENNY, PENNY, PENNY, PENNY, PENNY] | COKE     | [:] // exact change
        PEPSI   | [DIME, DIME]                                    | null     | [(DIME): 2] // trying pepsi, not enough funds, return money
        SODA    | [QUARTER, QUARTER]                              | SODA     | [(NICKEL): 1]
    }


}
