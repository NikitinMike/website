package com.example.website;

import java.io.File;
import java.util.Hashtable;

import static com.example.website.DataStreams.readLopatinFile;
import static com.example.website.DataStreams.readWordBookFile;

//@Repository
public class Dictionary {
    static Hashtable<String, String> wordTable = new Hashtable<>();

    public Dictionary() {
        if (wordTable.isEmpty()){
            for (String word : readWordBookFile(new File("\\DBWords\\wordbook.txt")))
                wordTable.put(word.trim().replaceAll("'", ""), word);
            for(String word:readLopatinFile(new File("\\DBWords\\lop1v2.utf8.txt")))
                wordTable.put(word.trim().replaceAll("`", ""), word);
        }
//        System.out.println("Dictionary:" + wordTable.get("только"));
        System.out.println("Dictionary words:" + wordTable.size());
//        wordTable.forEach((k,v) -> System.out.print(k + ":"+v+","));
    }

    public String getWord(String word) {
//        System.out.println(word);
        if (wordTable.containsKey(word)) word = wordTable.get(word);
        if (!word.contains("`")&&!word.contains("'"))
            if (word.length() - word.replaceAll("[ёуеыаоэяию]", "").length() == 1)
                word = word.replaceFirst("([ёуеыаоэяию])", "$1'");
            else word = word.replaceFirst("(ё)", "$1'");
        return word;
    }

    public String getGlas(String word){
        return getWord(word).replaceAll("[йцкнгшщзхъфвпрлджчсмтьб]", "");
    }
}