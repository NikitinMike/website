package com.example.website;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.website.Dictionary.getWord;
import static com.example.website.Utils.*;
import static java.lang.Math.random;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

//@Data
public class Sentence {
    String sentence;
    int amount;
    int[][] combines;
    String[] words; // слова
    int[] parts; // какой части речи принадлежит
//    Hashtable<String, List<WordBookEntity>> wordsEntityHashMap;

    public Sentence(String source) {
//        System.out.println(str); str.replaceAll("[^а-яА-ЯёЁ`']+", " ")
        sentence = source.replaceAll("[_,!.—?;:]+", " ");
        words = sentence.toLowerCase().replaceAll("[^а-яё`']+", " ").split("\\s+");
//        System.out.println(stream(words).sorted().collect(joining(",")));
//        wordsEntityHashMap = readWordBook(words);
//        System.out.println(wordsEntityHashMap);
//        if (wordsEntityHashMap != null) wordsEntityHashMap.forEach((k, v) -> System.out.println(v));
        amount = words.length;
        combines = new int[amount][amount];
        for (int i = 0; i < amount; i++) combines[0][i] = i;
    }

    String out(int[] a) {
        return stream(a).mapToObj(j -> wordAnalyse(getWord(words[j])))
                .filter(p -> !p.isEmpty())
                .map(w -> w.contains("'") || w.matches("[^ёуеыаоэяию]+") ? w : "<b>" + w + "</b>")
                .collect(joining(" "));
    }

    public List<String> outWords(int v) {
        if(combines.length>0)
            return stream(combines[v]).mapToObj(j -> words[j])
                    .filter(p -> !p.isEmpty()).collect(Collectors.toList());
        return new ArrayList<>();
    }

    String outStrip(int[] a) {
        return stream(a).mapToObj(j -> wordStrip(getGlas(getWord(words[j]))))
                .filter(p -> !p.isEmpty()).collect(joining())
                .replaceAll("-+", "")
                .replaceAll("[ёуеыаоэяию]", "-");
    }

    public String[] getHash(int v) {
        if (words.length == 0) return new String[]{"", sentence};
        return new String[]{outStrip(combines[v]), out(combines[v])};
    }

    public String randomOut(int v) {
        if (v > 0 && amount > 1) return out(combines[(int) (random() * amount)]);
        return out(combines[0]);
    }

    List<String> fullOut() {
        return stream(combines).map(this::out).collect(toList());
    }
}
