package com.arthurgrig.vendingMachine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CoinChangeHelper {

    public static Map<Coin, Integer> coinChange(Map<Coin, Integer> coinsQuantities, int amount) {
        // sorted denominations
        Coin[] coins = new Coin[coinsQuantities.size()];

        // sorted quantity for each denomination
        int[] coinsQ = new int[coinsQuantities.size()];

        Map<Coin, Integer> treeMap = new TreeMap<>(Collections.reverseOrder());
        treeMap.putAll(coinsQuantities);

        int i = 0;
        for (Map.Entry<Coin, Integer> e : treeMap.entrySet()) {
            coins[i] = e.getKey();
            coinsQ[i] = e.getValue();
            i++;
        }

        Map<Coin, Integer> acc = new HashMap<>();

        return dfsHelper(amount, coins, coinsQ, acc);
    }

    private static Map<Coin, Integer> dfsHelper(int amount, Coin[] coins, int[] coinsQ, Map<Coin, Integer> acc) {

        if (amount == 0) {
            return acc;
        }

        for (int i = 0; i < coins.length; i++) {
            Coin c = coins[i];

            if (amount - c.getValue() >= 0 && coinsQ[i] > 0) {
                coinsQ[i] = coinsQ[i] - 1;
                acc.put(c, acc.getOrDefault(c, 0) + 1);
                Map<Coin, Integer> res = dfsHelper(amount - c.getValue(), coins, coinsQ, acc);
                coinsQ[i] = coinsQ[i] - 1;

                if (res != null) {
                    return res;
                }

                acc.put(c, acc.get(c) - 1);
            }
        }

        return null;


    }


}
