package com.example.website;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Hashtable;
import java.util.Set;

import static com.example.website.DataStreams.readDictionary;
import static com.example.website.DataStreams.readLopatin;

@Repository
public class Dictionary {
    @Value("${dictionary.source}")
    final static String dir = "\\DBWords\\";
    static Hashtable<String, String> wordTable = new Hashtable<>();
    static boolean append = false;

    static void putWord(String word) {
        if (append) if (word.contains("`") || word.contains("'")) System.out.print(",");
        else System.out.print(word + ",");
        wordTable.put(word.trim().replaceAll("['`]", ""), word);
        if (word.contains("ё")) putWord(word.replaceAll("ё", "е'"));
    }

    public static void addWordSet(Set<String> words){words.forEach(Dictionary::putWord);}

    public Dictionary() {
        if (wordTable.isEmpty()) {
            for (String word : readLopatin(dir + "lop1v2.utf8.txt")) putWord(word);
            System.out.println("Lopatin's Dictionary words:" + wordTable.size());
            for (String word : readDictionary(dir + "wordbook.txt")) putWord(word);
            System.out.println("Dictionary words:" + wordTable.size());
        }
        System.out.println("Dictionary:" + wordTable.get("полей"));
//        wordTable.forEach((k,v) -> System.out.print(k + ":"+v+","));
        append = true;
    }

    public String getWord(String word) {
        if (wordTable.containsKey(word)) word = wordTable.get(word); // .replaceAll("['`]","")
        else putWord(word);  // System.out.print(word + ",");
        // if (!word.contains("ё") && word.contains("е"))
//      return getWord(reverse(reverse(word).replaceFirst("е", "ё")));
//      return getWord(word.replaceFirst("е", "ё"));
        if (!word.contains("`") && !word.contains("'"))
            if (word.length() - word.replaceAll("[ёуеыаоэяию]", "").length() == 1)
                word = word.replaceFirst("([ёуеыаоэяию])", "$1'");
            else word = word.replaceFirst("(ё)", "$1'");
        return word;
    }

    public String getGlas(String word) {
        return getWord(word).replaceAll("[йцкнгшщзхъфвпрлджчсмтьб]", "");
    }
}