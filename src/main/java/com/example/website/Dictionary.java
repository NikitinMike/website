package com.example.website;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.website.DataStreams.*;
import static com.example.website.Utils.glasCount;
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
    int level = 0;

    public Dictionary() {
        if (wordTable.isEmpty())
            switch (level) {
                case 0:
                    addSet("Dictionary words", readDictionary(dir + "wordbook.txt"));
                case 1:
                    addSet("Hagen words", readHagen(dir + "hagen-orf.txt"));
                case 2:
                    addSet("Lopatin words", readLopatin(dir + "lop1v2.utf8.txt"));
                case 3:
                    addSet("Big Thesaurus words", readThesaurus("thesaurus.txt"));
                case 4:
                    addSet("Small Thesaurus words", readThesaurus("texts\\thesaurus.txt"));
                default:
                    System.out.println("Dictionary:" + wordTable.size());
//                    wordTable.forEach((k, v) -> System.out.print(k + ":" + v + ","));
            }
        showThesaurus = true;
    }

    static public Set<String> getValues() {
//        return (Set<String>) wordTable.values();
        return wordTable.values().stream().filter(Objects::nonNull).collect(toSet());
    }

    static public List<String> getValues(String v) {
        return wordTable.values().stream().filter(w -> w.startsWith(v)).sorted().collect(Collectors.toList());
    }

    static void putWord(String word) {
//        if(glasCount(word)==1) System.out.print(word+",");
        if (!word.matches("[абвгдеёжзийклмнопрстуфхцчшщьъыэюя'`-]+")) return;
        if (word.matches("[^ёуеыаоэяию]*")) return; //  System.out.printf(" [%s] ",word);
//        if(word.matches("`[^ёуеыаоэяию]"))
//        word = word.replaceAll("([ёуеыаоэяию])`([^ёуеыаоэяию])","$1'$2");
        word = word.trim().replaceAll("`([ёуеыаоэяию])", "$1'");
//        if (word.contains("ё")) putWord(word.replaceAll("ё", "е'")); // Ё -> `E
        if (glasCount(word) == 1) word = word.replaceAll("([ёуеыаоэяию])", "$1'");
        word = word.replaceAll("''", "'");
//        word = word.replaceAll("--", "-");
        if (word.contains("'") || word.contains("ё"))
            wordTable.put(word.replace("'", ""), word);
        else if (showThesaurus) System.out.print(word + ",");
    }

    public static void addSet(String what, Set<String> words) {
        words.forEach(Dictionary::putWord);
        if (what != null) System.out.println(what + " + " + words.size() + "\t" + wordTable.size());
    }

    public static String getWord(String word) {
        word = word.replaceAll("`(.)", "$1'");
        if (wordTable.containsKey(word)) word = wordTable.get(word);
//        else putWord(word);
        if (!word.contains("'"))
            if (word.length() - word.replaceAll("[ёуеыаоэяию]", "").length() == 1)
                word = word.replaceFirst("([ёуеыаоэяию])", "$1'");
            else word = word.replaceFirst("(ё)", "$1'");
        // getWord(reverse(reverse(word).replaceFirst("е", "ё")));
        return word;
    }

    public static String check(String word) {
        return wordTable.get(word);
    }
}