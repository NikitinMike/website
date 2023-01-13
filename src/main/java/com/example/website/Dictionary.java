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

    public Dictionary() {
        if (wordTable.isEmpty()) {
            for (String word : readThesaurus("texts\\thesaurus.txt")) putWord(word);
            System.out.println("Thesaurus words:" + wordTable.size());
            for (String word : readLopatin(dir + "lop1v2.utf8.txt")) putWord(word);
            System.out.println("Lopatin's Dictionary words:" + wordTable.size());
            for (String word : readDictionary(dir + "wordbook.txt")) putWord(word);
            System.out.println("Dictionary words:" + wordTable.size());
        }
//        System.out.println("Dictionary:" + wordTable.get("полей"));
//        wordTable.forEach((k,v) -> System.out.print(k + ":"+v+","));
        showThesaurus = true;
    }

    static String putWord(String word) {
        if (word.matches("[^ёуеыаоэяию]*")) return word; //  System.out.printf(" [%s] ",word);
        word = word.trim().replaceAll("`(.)", "$1'");
        if (showThesaurus && !word.contains("'")) System.out.print(word + ",");
        wordTable.put(word.replaceAll("['`]", ""), word);
        if (word.contains("ё")) return putWord(word.replaceAll("ё", "е'"));
        return word;
    }

    public static void addWordSet(Set<String> words) {
        words.forEach(Dictionary::putWord);
    }

    public String getWord(String word) {
        word = word.replaceAll("`(.)", "$1'");
        if (wordTable.containsKey(word)) word = wordTable.get(word);
        else putWord(word);
        if (!word.contains("'"))
            if (word.length() - word.replaceAll("[ёуеыаоэяию]", "").length() == 1)
                word = word.replaceFirst("([ёуеыаоэяию])", "$1'");
            else word = word.replaceFirst("(ё)", "$1'");
        // getWord(reverse(reverse(word).replaceFirst("е", "ё")));
        return word;
    }

    public String getGlas(String word) {
        return getWord(word).replaceAll("[йцкнгшщзхъфвпрлджчсмтьб]", "");
    }
}