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
    int level = 3;

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

    static public Set<String> getValuesSet() {
//        return (Set<String>) wordTable.values();
        return wordTable.values().stream().filter(Objects::nonNull).collect(toSet());
    }

    static public List<String> getValuesList(String v) {
        return wordTable.values().stream().filter(w -> w.startsWith(v)).sorted().collect(Collectors.toList());
    }

    static void putWord(String word) {
//        if(glasCount(word)==1) System.out.print(word+",");
        if (!word.matches("[??????????????????????????????????????????????????????????????????'`-]+")) return;
        if (word.matches("[^????????????????????]*")) return; //  System.out.printf(" [%s] ",word);
//        if(word.matches("`[^????????????????????]"))
//        word = word.replaceAll("([????????????????????])`([^????????????????????])","$1'$2");
        word = word.trim().replaceAll("`([????????????????????])", "$1'");
//        if (word.contains("??")) putWord(word.replaceAll("??", "??'")); // ?? -> `E
        if (glasCount(word) == 1) word = word.replaceAll("([????????????????????])", "$1'");
        word = word.replaceAll("''", "'");
//        word = word.replaceAll("--", "-");
        if (word.contains("'") || word.contains("??"))
            wordTable.put(word.replace("'", ""), word);
        else if (showThesaurus) System.out.print(word + ",");
    }

    public static void addSet(String what, Set<String> words) {
        words.forEach(Dictionary::putWord);
        if (what != null) System.out.println(what + " + " + words.size() + "\t" + wordTable.size());
    }

    public static String getWord(String word) {
        word = word.replaceAll("`(.)", "$1'");
        if (wordTable.containsKey(word)) word = get(word); // else putWord(word);
        if (!word.contains("'"))
            if (word.length() - word.replaceAll("[????????????????????]", "").length() == 1)
                word = word.replaceFirst("([????????????????????])", "$1'");
            else word = word.replaceFirst("(??)", "$1'");
        // getWord(reverse(reverse(word).replaceFirst("??", "??")));
        return word;
    }

    public static String get(String word) {
        return wordTable.get(word);
    }
}