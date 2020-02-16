package com.arthurgrig.vendingMachine

import spock.lang.Specification
import spock.lang.Unroll

import static com.arthurgrig.vendingMachine.Coin.*

class CoinChangeHelperTest extends Specification {

    @Unroll
    def "CoinChange"() {
        given: "Quantities of coins and amount"
        coins
        amount

        when: "calculated"
        Map<Coin, Integer> result = CoinChangeHelper.coinChange(coins, amount)
        def sum = 0

        if (result != null) {
            result.entrySet().each { entry -> sum += (entry.key.value * entry.value) }
        }

        then: "returns expected quantities for each coin"
        println("sum: " + sum)
        println(result)
        result == expected

        if (result != null) {
            sum == amount
        }

        where:
        coins                                                      | amount | expected
        [(PENNY): (0), (NICKEL): 10, (DIME): 0, (QUARTER): 5]      | 10     | [(NICKEL): 2]
        [(PENNY): (10), (NICKEL): 0, (DIME): 0, (QUARTER): 5]      | 10     | [(PENNY): 10]
        [(PENNY): (10), (NICKEL): 10, (DIME): 10, (QUARTER): 10]   | 60     | [(DIME): 1, (QUARTER): 2]
        [(PENNY): (10), (NICKEL): 10, (DIME): 10, (QUARTER): 10]   | 100    | [(QUARTER): 4]
        [(PENNY): (10), (NICKEL): 10, (DIME): 10, (QUARTER): 10]   | 1000   | null
        [(PENNY): (1000), (NICKEL): 10, (DIME): 10, (QUARTER): 10] | 1000   | [(DIME): 10, (PENNY): 600, (NICKEL): 10, (QUARTER): 10]
    }
}
