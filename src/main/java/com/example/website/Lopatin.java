package com.example.website;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class Lopatin {

    static Set<String> readLopatinNew(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            Set<String> wordSet = new HashSet<>();
            do {
//                Set<String> partSet = new HashSet<>();
                String line = reader.readLine()
                        .replaceAll("\\(.+\\)?", " ")
                        .replaceAll(".+#","")
//                        .replaceAll(".+%","")
                        .toLowerCase();
                String[] words = line
                        .replaceAll("[а-я]+\\.","")
                        .replaceAll("[^а-яё`'-]+"," ")
//                        .trim();
                        .split("\\s+");
//                System.out.print(words+"; ");
//                if(!words[0].equals(words[1]))
//                List<String> wordSet = stream(words).collect(Collectors.toList());
//                wordSet.add(words[0]);
//                Collections.addAll(wordSet, words);
                stream(words).filter(word -> !word.matches("-.+")).forEach(wordSet::add);
//                System.out.println();
                System.out.println(words[0]+" "+stream(words).filter(word -> word.matches("-.+")).collect(Collectors.toSet()));
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
                if(word.contains(" ")||word.contains("-"))
                    System.out.println(stream(word.replaceAll("\\(.+\\)", " ").split("\\s+|-"))
                            .filter(w->!w.isEmpty())
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
