package com.example.website;

import java.util.*;

import static com.example.website.Dictionary.wordTable;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;

public class Rhythm {
    public static Map<String, Set<String>> getRhythm(int key) {
        Map<String, Set<String>> rhythm = new TreeMap<>(Comparator.comparing(Utils::reverse));
        for (String s : wordTable.values().stream().filter(Objects::nonNull).collect(toSet())) {

            if(key!=0) if(s.replaceAll("[^ёуеыаоэяию]","").length()!=key) continue;

            String w = new Sentence(s).getHash()[1].replaceFirst("`$","");
//            if(!w.contains(" "))continue;
            if(w.contains(" ")) continue;

            String ok = w.replaceFirst(".*-", "").replaceAll("(.)'", "$1");

//            ok = ok.replaceAll("[^ёуеыаоэяию](.{2,})", "$1");

//            if(ok.equals(w.replaceAll("'",""))) continue;
//            System.out.print(ok+":"+s+" ");

            if(ok.matches(".*[ёуеыаоэяию][^ёуеыаоэяию]+ь?$"))
                ok=ok.replaceAll(".*([ёуеыаоэяию][^ёуеыаоэяию]+ь?)$","$1");
//            if(ok.matches("[ёуеыаоэяию][^ёуеыаоэяию]ь?"))
//                ok=ok.replaceAll("[ёуеыаоэяию]","");
//            if(ok.matches("[^ёуеыаоэяию][ёуеыаоэяию]"))
//                ok=ok.replaceAll("[^ёуеыаоэяию]","");
//            if(ok.matches("[^ёуеыаоэяию]ж"))
//                ok=ok.replaceFirst("[^ёуеыаоэяию]","");
//            if(ok.matches("[мнл]м"))
//                ok=ok.replaceFirst("[^ёуеыаоэяию]","@");
            if(ok.matches("[ёуеыаоэяию][^ёуеыаоэяию]ь?[^ёуеыаоэяию]ь?"))
                ok=ok.replaceAll("[ёуеыаоэяию]","");

            if(ok.matches("[^ёуеыаоэяию]?[ёуеыаоэяию].{3,}"))
                ok = ok.replaceFirst("[^ёуеыаоэяию]?[ёуеыаоэяию](.+)","$1");
            if(ok.matches("ь.+")) ok = ok.replaceFirst("ь","");

            Set<String> set = rhythm.get(ok);
            if (set == null) rhythm.put(ok, new TreeSet<>(singleton(w)));
            else set.add(w);
        }
        return rhythm;
    }

}
