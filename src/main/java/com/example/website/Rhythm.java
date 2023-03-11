package com.example.website;

import java.util.*;

import static com.example.website.Dictionary.wordTable;
import static com.example.website.Utils.glasCount;
import static com.example.website.Utils.wordAnalyse;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;

public class Rhythm {

    static String reverseOk(String str) {
        return Utils.reverse(str.replace("`", ""));
    }

    public static Map<String, Set<String>> getRhythm(int key, String chr) {
        Map<String, Set<String>> rhythm = new TreeMap<>(Comparator.comparing(Rhythm::reverseOk));
//        Set<String> rhythmSet = new TreeSet<>(Comparator.comparing(Utils::getGlas));
//        for (String s : wordTable.values().stream().filter(Objects::nonNull).collect(toSet())) {
        for (String s : Dictionary.getValues()) {

            if (key != 0 && key != glasCount(s)) continue;

            String w = wordAnalyse(s) // new Sentence(s).getHash()[1].strip()
                    .replaceAll("(.)'", "`$1")
                    .replaceFirst("`+$", "");
//            if (!w.contains("-")) System.out.print(w + ",");
//            if (w.contains(" ")) continue;

            String ok = w.contains("-") ? w.replaceFirst(".*-", "")
                    : w.replaceFirst(".*`", "`");
//                    : w.replaceFirst(".+([ёуеыаоэяию].*)", "$1");

//            if (!ok.matches(chr + "$")) continue;

            if (ok.matches("[^ёуеыаоэяию][ёуеыаоэяию][^ёуеыаоэяию]"))
                ok = ok.replaceFirst("[^ёуеыаоэяию]", "");

            if (ok.length() > 3)
                ok = ok.replaceFirst(".*`", "`");

            if (ok.length() > 3)
                ok = ok.replaceFirst("[^ёуеыаоэяию](.+)", "$1");

//            if (!ok.contains("`")) ok = ok.replace("ю", "у")
//                    .replace("ё", "о")
//                    .replace("я","а")
//                    ;

//            if(ok.matches("`.")) ok = ok.replaceAll("`", "");

//            ok = ok.replaceAll("(.)'", "`$1");
            ok = ok.replaceAll("['`]", "");
            Set<String> set = rhythm.get(ok);
            w = w.replaceAll("(.)'", "`$1");
            if (set != null) set.add(w);
            else rhythm.put(ok, new TreeSet<>(singleton(w)));
//            else set = rhythm.put(ok, new TreeSet<>(Comparator.comparing(
//                    word -> Utils.getGlas(word).length())));
//            if (set != null) set.add(w);
        }
        return rhythm;
    }

    public static Map<String, Set<String>> getRhythmOld(int key) {
        Map<String, Set<String>> rhythm = new TreeMap<>(Comparator.comparing(Utils::reverse));
        for (String s : wordTable.values().stream().filter(Objects::nonNull).collect(toSet())) {

            if (key != 0) if (s.replaceAll("[^ёуеыаоэяию]", "").length() != key) continue;

            String w = new Sentence(s).getHash()[1].replaceFirst("`$", "");
//            if(!w.contains(" "))continue;
            if (w.contains(" ")) continue;

            String ok = w.replaceFirst(".*-", "").replaceAll("(.)'", "$1");

//            ok = ok.replaceAll("[^ёуеыаоэяию](.{2,})", "$1");

//            if(ok.equals(w.replaceAll("'",""))) continue;
//            System.out.print(ok+":"+s+" ");

            if (ok.matches(".*[ёуеыаоэяию][^ёуеыаоэяию]+ь?$"))
                ok = ok.replaceAll(".*([ёуеыаоэяию][^ёуеыаоэяию]+ь?)$", "$1");
//            if(ok.matches("[ёуеыаоэяию][^ёуеыаоэяию]ь?"))
//                ok=ok.replaceAll("[ёуеыаоэяию]","");
//            if(ok.matches("[^ёуеыаоэяию][ёуеыаоэяию]"))
//                ok=ok.replaceAll("[^ёуеыаоэяию]","");
//            if(ok.matches("[^ёуеыаоэяию]ж"))
//                ok=ok.replaceFirst("[^ёуеыаоэяию]","");
//            if(ok.matches("[мнл]м"))
//                ok=ok.replaceFirst("[^ёуеыаоэяию]","@");
            if (ok.matches("[ёуеыаоэяию][^ёуеыаоэяию]ь?[^ёуеыаоэяию]ь?"))
                ok = ok.replaceAll("[ёуеыаоэяию]", "");

            if (ok.matches("[^ёуеыаоэяию]?[ёуеыаоэяию].{3,}"))
                ok = ok.replaceFirst("[^ёуеыаоэяию]?[ёуеыаоэяию](.+)", "$1");
            if (ok.matches("ь.+")) ok = ok.replaceFirst("ь", "");

            Set<String> set = rhythm.get(ok);
            if (set == null) rhythm.put(ok, new TreeSet<>(singleton(w)));
            else set.add(w);
        }
        return rhythm;
    }

}
