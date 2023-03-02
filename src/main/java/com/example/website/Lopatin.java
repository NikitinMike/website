package com.example.website;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.website.Utils.glasCount;
import static com.example.website.Utils.reverse;
import static java.util.Arrays.stream;

public class Lopatin {

    public static Set<String> wordExtend(String[] words) {
        Set<String> wordSet = new HashSet<>();
        if (words == null || words.length == 0) return wordSet;
        String baseWord = null;
        for (String word : words)
            if (word.matches(".")) ;
            else if (word.matches("-.+")) {
                String o = word.substring(1); // окончание
                if (baseWord != null)
                    if (o.matches("[`ёуеыаоэяию].*")) {
                        String w = join(baseWord, o);
                        if (w.contains("+")) System.out.println(baseWord + " [" + o + "] " + w);
                        else wordSet.add(w);
                    } else wordSet.add(joinOk(baseWord, o));
            } else if (word.matches(".*-")) System.out.print(word + ","); // check pipe
            else if (word.contains("`") || word.contains("ё") || glasCount(word) == 1) {
                wordSet.add(word);
                baseWord = word;
            } else wordSet.add(word.replaceFirst("([ёуеыаоэяию])", "`$1"));
        return wordSet;
    }

    private static String joinOk(String word, String o) {
        int l = word.lastIndexOf(o.charAt(0));
        if (l < 0) return word;
        String subword = word.substring(l);
        if (subword.contains("`")) o = o.replaceFirst("([ёуеыаоэяию])", "`$1");
        String w = word.replaceFirst(subword, o).replaceFirst("``", "`");
        if (!w.contains("`") && !w.contains("ё")) System.out.printf(" %s [%s] %s, ", word, o, w);
        return w;
    }

    private static String join(String word, String o) {
        if (o.matches("`.+")) {
            if (o.matches("`[ёуеыаоэяию]"))
                if (word.matches(".*[ьёуеыаоэяию]$"))
                    return reverse(reverse(word).replaceFirst("[ьёуеыаоэяию]", o));
                else if (word.matches(".+[^ёуеыаоэяию]$")) return word + o;
            return word.replaceFirst("(.+)`.*", "$1") + o + ", ";
        } else if (word.matches(".+[ёуеыаоэяию]$"))
            return word.replaceFirst("(.+)[ёуеыаоэяию]$", "$1") + o;
        else if (word.matches(".+ь$"))
            return word.replaceFirst("(.+)ь$", "$1") + o;
        else if (word.matches(".+й$"))
            if (o.contains("я")) return word + o;
            else if (word.contains("ный")) return word.replaceFirst("..ный", o);
            else if (word.contains("ий")) return word.replaceFirst("ий", o);
            else if (word.contains("ый")) return word.replaceFirst("ый", o);
            else if (word.contains("ей") && o.matches("и|а|ю|е.")) return word.replaceFirst("ей", o);
            else if (word.contains("ой") && o.matches("а|ю|ы|е.|ёв")) return word.replaceFirst("ой", o);
            else if (word.contains("ай") && o.matches("ю")) return word.replaceFirst("ай", o);
            else return word + " +" + o;
        return word + o;
    }

    static Set<String> readLopatinNew(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            Set<String> wordSet = new HashSet<>();
            do {
//                Set<String> partSet = new HashSet<>();
                String line = reader.readLine()
                        .replaceAll("\\(.+\\)?", " ")
                        .replaceAll(".+#", "")
//                        .replaceAll(".+%","")
                        .toLowerCase();
                String[] words = line
                        .replaceAll("[а-я]+\\.", "")
                        .replaceAll("[^а-яё`'-]+", " ")
//                        .trim();
                        .split("\\s+");
//                System.out.print(words+"; ");
//                if(!words[0].equals(words[1]))
//                List<String> wordSet = stream(words).collect(Collectors.toList());
//                wordSet.add(words[0]);
//                Collections.addAll(wordSet, words);
                stream(words).filter(word -> !word.matches("-.+")).forEach(wordSet::add);
//                System.out.println();
                System.out.println(words[0] + " " + stream(words).filter(word -> word.matches("-.+")).collect(Collectors.toSet()));
//                wordSet.addAll(stream(words).filter(w->(w.contains("`")||w.contains("ё"))).collect(Collectors.toSet()));
//                System.out.println(stream(words).collect(Collectors.toSet()));
//                if(!partSet.isEmpty()) System.out.print(wordSet+":"+partSet+" ");
            } while (reader.ready());
//            System.out.print(wordSet);
            return wordSet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void printTrash(Set<String> set) {
        set.stream().filter(s -> !(s.contains("`") || s.contains("ё")))
                .filter(w -> w.replaceAll("[^уеыаоэяию]", "").length() > 1)
                .map(s -> s + ",").forEach(System.out::print);
        System.out.println();
    }

    static Set<String> readLopatin(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            Set<String> wordSet = new HashSet<>();
            Set<String> wordSet2 = new HashSet<>();
            Set<String> wordSet3 = new HashSet<>();
            do {
                String[] line = reader.readLine().split("%");
//                if(line[1].contains("пишется")) continue;
//                if(line[1].contains("частица")) continue;
//                if(line[1].contains("предлог")) continue;
//                if(line[1].contains("приставка")) continue;
//                if(line[1].contains("союз")) continue;
//                if(line[1].contains("...")) continue;
//                    System.out.println(line[1]);

                String word = line[0].split("#")[1];
                if (word.contains(" ") || word.contains("-"))
                    System.out.println(stream(word.replaceAll("\\(.+\\)", " ").split("\\s+|-"))
                            .filter(w -> !w.isEmpty())
//                            .filter(w -> w.replaceAll("[^уеыаоэяию]", "").length() > 1)
//                            .filter(s -> !(s.contains("`") || s.contains("ё")))
                            .collect(Collectors.toSet()));
                else wordSet.add(word);

                String[] words = line[1].replaceAll("\\(.+\\)?", " ")
//                        .replaceAll("но","")
                        .toLowerCase().split("[,;:]| и ");
                if (words.length > 1) stream(words).map(String::trim)
                        .filter(w -> w.matches("[а-яё`']+"))
                        .filter(w -> w.replaceAll("[^уеыаоэяию]", "").length() > 1)
                        .forEach(wordSet2::add);
                lopatinExtender(words, wordSet3);
            } while (reader.ready());
            printTrash(wordSet);
            printTrash(wordSet2);
            printTrash(wordSet3);
            wordSet.addAll(wordSet2);
            return wordSet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void lopatinExtender(String[] words, Set<String> wordSet) {
        if (words.length < 2) return;
        if (words[0].matches("[а-яё`']+"))
            for (String word : words)
//                if (word.contains(":"))
                if (word.matches("-[а-яё`']+")) ; // wordSet.add(w);
                else if (!wordSet.contains(word.trim())) {
                    String w = word.replaceAll("[а-я]+\\s?\\.", "").trim();
                    if (!w.isEmpty())
                        if (w.matches("[а-яё`']+"))
//                            if (w.replaceAll("[^уеыаоэяию]","").length()>1)
                            wordSet.add(w);
//                            else System.out.print(w+"|");
                        else if (w.matches("-[а-яё`']+")) ; // wordSet.add(w);
//                        else System.out.printf(" %s: [%s] %s\n", words[0], w, word);
                }
    }
}
