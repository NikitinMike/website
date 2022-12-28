package com.example.website;

import java.io.File;
import java.util.Hashtable;

import static com.example.website.DataStreams.readWordBookFile;

//@Repository
public class Dictionary {
    static Hashtable<String, String> wordTable = new Hashtable<>();

    public Dictionary() {
        if (wordTable.isEmpty())
            for (String word : readWordBookFile(new File("D:\\DBWords\\wordbook.txt")))
                wordTable.put(word.trim().replaceAll("'", ""), word);
        System.out.println("Dictionary words:" + wordTable.size());
//        wordTable.forEach((k,v) -> System.out.print(k + ":"+v+","));
    }

    public String getWord(String word) {
//        System.out.println(word);
        if (wordTable.containsKey(word)) word = wordTable.get(word);
        //            if (word.matches(".*([её]).*"))
        if (!word.contains("'"))
            if (word.replaceAll("[^ёуеыаоэяию]+", "").length() == 1)
                word = word.replaceFirst("([ёуеыаоэяию])", "$1'");
            else word = word.replaceFirst("([ёе])", "$1'");
        return word;
    }
}