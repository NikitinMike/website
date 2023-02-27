package com.example.website;

import static com.example.website.Utils.factorial;
import static com.example.website.Utils.swapClone;
import static java.lang.Math.random;

public class Combiner {

    int amount;
    int[][] combines;

    public int[][] getCombines() {return combines;}

    public Combiner(int length) {
        combines = new int[factorial(length)][length];
        for (int i = 0; i < length; i++) combines[0][i] = i;
        amount = length > 1 ? combInit(length) : 1;
    }

    public int[] getRandom() {
        if (amount > 1) return combines[(int) (random() * amount)];
        return combines[0];
    }

    int combInit(int n) {
        if (n > 2) {
            int nf = combInit(n - 1);
            for (int i = 0; i < nf; i++)
                for (int j = 1; j < n; j++)
                    combines[nf * j + i] = swapClone(combines[nf * (j - 1) + i], n - j);
            return nf * n;
        }
        // N=2
        combines[1] = swapClone(combines[0], 1);
        return 2;
    }
}
