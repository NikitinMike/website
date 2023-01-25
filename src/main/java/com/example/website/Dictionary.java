package com.example.website;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Hashtable;
import java.util.Set;

import static com.example.website.DataStreams.*;

@Repository
public class Dictionary {
    @Value("${dictionary.source}")
    final static String dir = "\\DBWords\\";
    static Hashtable<String, String> wordTable = new Hashtable<>();
    static boolean showThesaurus = false;
// Comparator<Human> nameComparator = (h1, h2) -> h1.getName().compareTo(h2.getName());

    public Dictionary() {
        if (wordTable.isEmpty())
            switch (0) {
                case 0:
                    addWordSet(readDictionary(dir + "wordbook.txt"));
                    System.out.println("Dictionary words:" + wordTable.size());
                case 1:
                    addWordSet(readHagen(dir + "hagen-orf.txt"));
                    System.out.println("Hagen words:" + wordTable.size());
                case 2:
                    addWordSet(readLopatin(dir + "lop1v2.utf8.txt"));
                    System.out.println("Lopatin words:" + wordTable.size());
                case 3:
                    addWordSet(readThesaurus("thesaurus.txt"));
                    System.out.println("Big Thesaurus words:" + wordTable.size());
                case 4:
                    addWordSet(readThesaurus("texts\\thesaurus.txt"));
                    System.out.println("Small Thesaurus words:" + wordTable.size());
                default:
//                    wordTable.forEach((k, v) -> System.out.print(k + ":" + v + ","));
//                    System.out.println("Dictionary:" + wordTable.get("её"));
            }
        showThesaurus = true;
    }

    static void putWord(String word) {
        if (word.matches("[^ёуеыаоэяию]*")) return; //  System.out.printf(" [%s] ",word);
        word = word.trim().replaceAll("`(.)", "$1'");
        if (word.contains("ё")) putWord(word.replaceAll("ё", "е'"));
        if (word.replaceAll("([^ёуеыаоэяию'])", "").length() == 1)
            word = word.replaceAll("([ёуеыаоэяию])", "$1'");
        word = word.replaceAll("''", "'");
        if (word.contains("'") || word.contains("ё"))
            wordTable.put(word.replace("'", ""), word);
        else if (showThesaurus) System.out.print(word + ",");
    }

    public static void addWordSet(Set<String> words) {
        words.forEach(Dictionary::putWord);
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