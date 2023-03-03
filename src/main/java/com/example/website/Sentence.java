package com.example.website;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.website.Dictionary.getWord;
import static com.example.website.Utils.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

//@Data
public class Sentence {
    String sentence;
    int amount;
    int[] combine;
    String[] words; // слова
//    int[] parts; // какой части речи принадлежит
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
        combine = new int[amount];
        for (int i = 0; i < amount; i++) combine[i] = i;
    }

    String out(int[] a) {
        return stream(a).mapToObj(j -> wordAnalyse(getWord(words[j])))
                .filter(p -> !p.isEmpty())
                .map(w -> w.contains("'") || w.matches("[^ёуеыаоэяию]+") ? w : "<b>" + w + "</b>")
                .collect(joining(" "));
    }

    public List<String> outWords() {
        if (combine.length == 0) return new ArrayList<>();
        return stream(combine).mapToObj(j -> words[j]).filter(p -> !p.isEmpty()).collect(toList());
    }

    String outStrip(int[] a) {
        return stream(a).mapToObj(j -> wordStrip(getGlas(getWord(words[j]))))
                .filter(p -> !p.isEmpty()).collect(joining())
                .replaceAll("-+", "")
                .replaceAll("[ёуеыаоэяию]", "-");
    }

    public String[] getHash() {
        if (words.length < 2) return new String[]{"", sentence};
        return new String[]{outStrip(combine), out(combine)};
    }

    public String random(boolean random) {
        if (random) return out(new Combiner(amount).getRandom());
        return out(combine);
    }

    List<String> out(boolean full) {
        if (full) return Collections.singletonList(out(combine));
        return stream(new Combiner(amount).getCombines())
                .limit(999).map(this::out).collect(toList());
    }
}
