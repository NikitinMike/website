package com.example.website;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DataStreams extends DataStrings {
    String[][] in = {hymn, sobaka, vorona, chuchelo, rossia, pushkin, tutchev};

    static Hashtable<String, List<WordsBookEntity>> readWordsBookDB(WordsBookRepository repository, String[] words) {
        Hashtable<String, List<WordsBookEntity>> wordsEntityHashMap = new Hashtable<>();
        for (String word : words) {
//            String [] subWords = word.split("_");
//            if(subWords.length>1) wordsEntity.addAll(readWordsBook(subWords));
//            else wordsEntity.addAll(repository.findAllByWord(word));
            List<WordsBookEntity> wordsBookEntities = repository.findAllByWord(word);
            if (wordsBookEntities == null || wordsBookEntities.isEmpty())
                wordsBookEntities = Collections.singletonList(new WordsBookEntity(word));
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
            do {
                String[] lines = reader.readLine().split("%");
                String[] words = lines[0].split("#");
                wordSet.add(words[1]);
            } while (reader.ready());
            return wordSet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    List<String> getTextResource(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            assert inputStream != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
            return reader.lines() // .map(s -> s.replaceAll("[-\"'_,!.—?:;\\d\\s]+", " ")
                    .map(s -> s.replaceAll("[^а-яА-ЯёЁ`']+", " ").trim())
                    .filter(p -> !p.isEmpty()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Set<String> extractWordSet(String file) throws IOException {
        return getTextStream(file).map(String::toLowerCase)
                .flatMap(l -> Arrays.stream(l.replaceAll("[^а-яё`']+", " ").split("\\s+")))
                .filter(w -> w.contains("'") || w.contains("`"))
                .map(w->w.replaceAll("`(.)","$1'"))
                .collect(Collectors.toSet());
    }
}