package com.example.website;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class DataStreams extends DataStrings {
    String[][] in = {hymn, sobaka, vorona, chuchelo, rossia, pushkin, tutchev};
    static WordsBookRepository wordsBookRepository;

    static Hashtable<String, List<WordBookEntity>> readWordBook(String[] words) {
        Hashtable<String, List<WordBookEntity>> wordsEntityHashMap = new Hashtable<>();
        for (String word : words) {
//            String [] subWords = word.split("_");
//            if(subWords.length>1) wordsEntity.addAll(readWordsBook(subWords));
//            else wordsEntity.addAll(repository.findAllByWord(word));
            List<WordBookEntity> wordsBookEntities = wordsBookRepository.findAllByWord(word);
            if (wordsBookEntities == null || wordsBookEntities.isEmpty())
                wordsBookEntities = Collections.singletonList(new WordBookEntity(word));
//            System.out.println(wordsBookEntities);
            wordsEntityHashMap.put(word, wordsBookEntities);
        }
        return wordsEntityHashMap;
    }

    static Set<String> readDictionary(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            Set<String> wordSet = new HashSet<>();
            do {
                String[] lines = reader.readLine().split("#");
                Collections.addAll(wordSet, lines[1].split(","));
            } while (reader.ready());
            return wordSet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Set<String> readThesaurus(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            Set<String> wordSet = new HashSet<>();
            do {
                Collections.addAll(wordSet, reader.readLine().split(","));
            } while (reader.ready());
            return wordSet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Stream<String> getTextStream(String fileName) throws IOException {
        return Files.newBufferedReader(Path.of(fileName)).lines().map(String::trim);
    }

    static Set<String> readLopatin(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            Set<String> wordSet = new HashSet<>();
            Set<String> wordSet2 = new HashSet<>();
            Set<String> wordSet3 = new HashSet<>();
            do {
                String[] lines = reader.readLine().split("%");
                String[] words = lines[0].split("#");
                wordSet.add(words[1]);
                words = lines[1].toLowerCase().split(",");
                if (words.length > 1) stream(words).map(String::trim)
                        .filter(word -> word.matches("[а-яё`']+"))
                        .filter(w->w.replaceAll("[^уеыаоэяию]","").length()>1)
                        .forEach(wordSet::add);
                lopatinWordsExtender(lines[1].toLowerCase(), wordSet3);
            } while (reader.ready());
            wordSet3.stream()
                    .filter(s -> !s.contains("`") && !s.contains("ё"))
                    .map(s -> s + ",").forEach(System.out::print);
            return wordSet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void lopatinWordsExtender(String line, Set<String> wordSet) {
        String[] words = line.replaceAll("\\(.+\\)", "")
                .split("[,;:]\\s?|\\sи\\s");
        if (words.length < 2) return;
        if (words[0].matches("[а-яё`']+"))
            for (String word : words)
                if (word.matches("-[а-яё`']+")) ; // wordSet.add(w);
                else if (!wordSet.contains(word.trim())) {
                    String w = word.replaceAll("[а-я]+\\s?\\.", "").trim();
                    if (!w.isEmpty())
                        if (w.matches("[а-яё`']+")) wordSet.add(w);
                        else if (w.matches("-[а-яё`']+")) ; // wordSet.add(w);
                        else System.out.printf(" %s: [%s] %s\n", words[0], w, word);
                }
    }

    Set<String> extractWordSet(String file) throws IOException {
        return getTextStream(file).map(String::toLowerCase)
                .flatMap(l -> stream(l.replaceAll("[^а-яё`']+", " ").split("\\s+")))
                .filter(w -> w.contains("'") || w.contains("`"))
                .map(w -> w.replaceAll("`(.)", "$1'"))
                .collect(Collectors.toSet());
    }
}