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

@Data
public class Sentence {

    int amount;
    int[][] sentence;
    String[] words; // слова
    int[] parts; // какой части речи принадлежит
    Hashtable<String, List<WordsBookEntity>> wordsEntityHashMap;
    static private Dictionary dictionary = new Dictionary();

    public Sentence(String str, WordsBookRepository repository) {
//        System.out.println(str);
        words = str.trim().replaceAll("[_,!.—?;:]+", " ").toLowerCase().split("\\s+");
        if (repository != null) wordsEntityHashMap = readWordsBookDB(repository, words);
//        System.out.println(wordsEntityHashMap);
//        if (wordsEntityHashMap != null) wordsEntityHashMap.forEach((k, v) -> System.out.println(v));
        amount = words.length;
        sentence = new int[amount][amount];
        for (int i = 0; i < amount; i++) sentence[0][i] = i;
    }

    String out(int[] a) {
        return stream(a).mapToObj(j -> wordAnalyse(dictionary.getWord(words[j])))
                .filter(p -> !p.isEmpty()).collect(joining(" "));
    }

    String outStrip(int[] a) {
        return stream(a).mapToObj(j -> wordStrip(dictionary.getGlas(words[j])))
                .filter(p -> !p.isEmpty()).collect(joining())
                .replaceAll("-+", "");
    }

    public String[] getHash(int v) {
        return new String[]{outStrip(sentence[v]), out(sentence[v])};
    }

    public String randomOut(int v) {
        if (v > 0 && amount > 1) return out(sentence[(int) (random() * amount)]);
        return out(sentence[0]);
    }

    List<String> fullOut() {
        return stream(sentence).map(this::out).collect(toList());
    }

}
