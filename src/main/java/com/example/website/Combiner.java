package com.example.website;

import lombok.Data;

import java.util.Hashtable;
import java.util.List;

import static com.example.website.DataStreams.readWordsBookDB;
import static com.example.website.Utils.*;
import static java.lang.Math.random;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

//@Slf4j
@Data
public class Combiner {

    int amount;
    int[][] comb;
    String[] words; // слова
    int[] parts; // какой части речи принадлежит
    Hashtable<String, List<WordsBookEntity>> wordsEntityHashMap;

    public Combiner(String str, WordsBookRepository repository) {
//        System.out.println(str);
        words = str.trim().toLowerCase().split("\\s+");
        if (repository != null) wordsEntityHashMap = readWordsBookDB(repository, words);
//        System.out.println(wordsEntityHashMap);
        if (wordsEntityHashMap != null) wordsEntityHashMap.forEach((k, v) -> System.out.println(v));
        comb = new int[factorial(words.length)][words.length];
        for (int i = 0; i < words.length; i++) comb[0][i] = i;
        amount = words.length > 1 ? combiner(words.length) : 1;
    }

    String out(int[] a) {
        return stream(a).mapToObj(j -> wordSplit(words[j]) + ' ').collect(joining());
    }

    int combiner(int n) {
        if (n > 2) {
            int nf = combiner(n - 1);
            for (int i = 0; i < nf; i++)
                for (int j = 1; j < n; j++)
                    comb[nf * j + i] = swapClone(comb[nf * (j - 1) + i], n - j);
            return nf * n;
        }
        // N=2
        comb[1] = swapClone(comb[0], 1);
        return 2;
    }

    public String randomOut(int v) {
//        for (String s:words) System.out.print(s+",");System.out.println(words.length);
        if (v == 0) return out(comb[0]);
        if (words.length > 3) return out(comb[(int) (random() * words.length)]);
        return out(comb[(int) (random() * 2)]);
//        return out(comb[(int)(Math.random()*amount)]);
    }

    List<String> fullOut() {
        return stream(comb).map(this::out).collect(toList());
    }

}
