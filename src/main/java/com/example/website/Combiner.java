package com.example.website;

import static com.example.website.Utils.factorial;
import static com.example.website.Utils.swapClone;
import static java.lang.Math.random;

public class Combiner {

    int amount;
    int[][] sentence;

    public Combiner(int length) {
        sentence = new int[factorial(length)][length];
        for (int i = 0; i < length; i++) sentence[0][i] = i;
        amount = length > 1 ? combInit(length) : 1;
    }

    public int[] randomSentence(int v) {
        if (v > 0 && amount > 1) return sentence[(int) (random() * amount)];
        return sentence[0];
    }

    int combInit(int n) {
        if (n > 2) {
            int nf = combInit(n - 1);
            for (int i = 0; i < nf; i++)
                for (int j = 1; j < n; j++)
                    sentence[nf * j + i] = swapClone(sentence[nf * (j - 1) + i], n - j);
            return nf * n;
        }
        // N=2
        sentence[1] = swapClone(sentence[0], 1);
        return 2;
    }
}
