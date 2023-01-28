package com.example.website;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.example.website.DataStreams.*;
import static com.example.website.Utils.glasCount;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;

@Repository
public class Dictionary {
    static Hashtable<String, String> wordTable = new Hashtable<>();
    static boolean showThesaurus = false;
    // Comparator<Human> nameComparator = (h1, h2) -> h1.getName().compareTo(h2.getName());
//    static Map<String, Set<String>> rhythm = new TreeMap<>(Comparator.comparing(Utils::reverse));
    @Value("${dictionary.source}")
    String dir = "\\DBWords\\";
    @Value("${dictionary.level}")
    int level=2;
    static boolean extendE = true;

    public Dictionary() {
        if (wordTable.isEmpty())
            switch (level) {
                case 0: addSet("Dictionary words", readDictionary(dir + "wordbook.txt"));
                case 1: addSet("Hagen words", readHagen(dir + "hagen-orf.txt"));
                case 2: addSet("Lopatin words",readLopatin(dir + "lop1v2.utf8.txt"));
                case 3: addSet("Small Thesaurus words",readThesaurus("texts\\thesaurus.txt"));
                case 4: addSet("Big Thesaurus words",readThesaurus("thesaurus.txt"));
                default: System.out.println("Dictionary:" + wordTable.size());
//                    wordTable.forEach((k, v) -> System.out.print(k + ":" + v + ","));
            }
        showThesaurus = true;
    }

    static void putWord(String word) {
        if (word.matches("[^ёуеыаоэяию]*")) return; //  System.out.printf(" [%s] ",word);
        word = word.trim().replaceAll("`(.)", "$1'");
        if (extendE) if (word.contains("ё")) putWord(word.replaceAll("ё", "е'"));
        if (glasCount(word) == 1) word = word.replaceAll("([ёуеыаоэяию])", "$1'");
        word = word.replaceAll("''", "'");
        if (word.contains("'") || word.contains("ё"))
            wordTable.put(word.replace("'", ""), word);
        else if (showThesaurus) System.out.print(word + ",");
    }

    public static void addSet(String what,Set<String> words) {
        words.forEach(Dictionary::putWord);
        if(what!=null) System.out.println(what+" + "+words.size() + "\t" + wordTable.size());
    }

    public static Map<String, Set<String>> getRhythm(int key) {
        Map<String, Set<String>> rhythm = new TreeMap<>(Comparator.comparing(Utils::reverse));
        for (String s : wordTable.values().stream().filter(Objects::nonNull).collect(toSet())) {

            if(key!=0) if(s.replaceAll("[^ёуеыаоэяию]","").length()!=key) continue;

            String w = new Sentence(s).getHash(0)[1];
//            if(!w.contains(" "))continue;

            String ok = w.replaceAll(".*-", "").replaceAll("(.)'", "$1");
            ok = ok.replaceAll("[^ёуеыаоэяию](.{2,})", "$1");

            if(ok.matches(".*[ёуеыаоэяию][^ёуеыаоэяию]+ь?$"))
                ok=ok.replaceAll(".*([ёуеыаоэяию][^ёуеыаоэяию]+ь?)$","$1");

            if(ok.equals(w.replaceAll("'",""))) continue;
//            System.out.print(ok+":"+s+" ");

            Set<String> set = rhythm.get(ok);
            if (set == null) rhythm.put(ok, new TreeSet<>(singleton(w)));
            else set.add(w);
        }
        return rhythm;
    }

    public static String getWord(String word) {
        word = word.replaceAll("`(.)", "$1'");
        if (wordTable.containsKey(word)) word = wordTable.get(word); else putWord(word);
        if (!word.contains("'"))
            if (word.length() - word.replaceAll("[ёуеыаоэяию]", "").length() == 1)
                word = word.replaceFirst("([ёуеыаоэяию])", "$1'");
            else word = word.replaceFirst("(ё)", "$1'");
        // getWord(reverse(reverse(word).replaceFirst("е", "ё")));
        return word;
    }
}