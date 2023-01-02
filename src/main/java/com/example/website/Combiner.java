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
    int[][] sentence;
    String[] words; // слова
    int[] parts; // какой части речи принадлежит
    Hashtable<String, List<WordsBookEntity>> wordsEntityHashMap;
    static private Dictionary dictionary = new Dictionary();

    public Combiner(String str, WordsBookRepository repository) {
//        System.out.println(str);
        words = str.trim().toLowerCase().split("\\s+");
        if (repository != null) wordsEntityHashMap = readWordsBookDB(repository, words);
//        System.out.println(wordsEntityHashMap);
//        if (wordsEntityHashMap != null) wordsEntityHashMap.forEach((k, v) -> System.out.println(v));
        sentence = new int[factorial(words.length)][words.length];
        for (int i = 0; i < words.length; i++) sentence[0][i] = i;
        amount = words.length > 1 ? combiner(words.length) : 1;
    }

    String out(int[] a) {
//        return stream(a).mapToObj(j -> wordSplit(words[j]) + ' ').collect(joining());
//        return stream(a).mapToObj(j -> words[j] + ' ').collect(joining());
        return stream(a).mapToObj(j -> wordAnalyse(dictionary.getWord(words[j])))
                .filter(p -> !p.isEmpty()).collect(joining(" "));
    }

    String outStrip(int[] a) {
        return stream(a).mapToObj(j -> wordStrip(dictionary.getWord(words[j])))
                .filter(p -> !p.isEmpty()).collect(joining(" "));
    }

    public String[] getHash(int v){
        return new String[]{outStrip(sentence[v]), out(sentence[v])};
    }

    int combiner(int n) {
        if (n > 2) {
            int nf = combiner(n - 1);
            for (int i = 0; i < nf; i++)
                for (int j = 1; j < n; j++)
                    sentence[nf * j + i] = swapClone(sentence[nf * (j - 1) + i], n - j);
            return nf * n;
        }
        // N=2
        sentence[1] = swapClone(sentence[0], 1);
        return 2;
    }

    public String randomOut(int v) {
//        for (String s:words) System.out.print(s+",");System.out.println(words.length);
        if (v == 0) return out(sentence[0]);
        if (words.length > 1) return out(sentence[(int) (random() * amount)]);
        return out(sentence[0]);
//        return out(comb[(int)(Math.random()*amount)]);
    }

    List<String> fullOut() {
        return stream(sentence).map(this::out).collect(toList());
    }

}
