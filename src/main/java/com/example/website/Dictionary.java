package com.example.website;

import java.io.File;
import java.util.Hashtable;

import static com.example.website.DataStreams.readLopatinFile;
import static com.example.website.DataStreams.readWordBookFile;
import static com.example.website.Utils.reverse;

//@Repository
public class Dictionary {
    static Hashtable<String, String> wordTable = new Hashtable<>();

    void putWord(String word){
        wordTable.put(word.trim().replaceAll("['`]", ""), word);
        if(word.contains("ё")) putWord(word.replaceAll("ё","е"));
    }

    public Dictionary() {
        if (wordTable.isEmpty()) {
            for (String word : readLopatinFile(new File("\\DBWords\\lop1v2.utf8.txt")))
                putWord(word);
            for (String word : readWordBookFile(new File("\\DBWords\\wordbook.txt")))
                putWord(word);
        }
//        System.out.println("Dictionary:" + wordTable.get("где"));
        System.out.println("Dictionary words:" + wordTable.size());
//        wordTable.forEach((k,v) -> System.out.print(k + ":"+v+","));
    }

    public String getWord(String word) {
        if (wordTable.containsKey(word.replaceAll("['`]","")))
            word = wordTable.get(word.replaceAll("['`]",""));
        else if (!word.contains("ё") && word.contains("е")) System.out.print(word + ",");
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