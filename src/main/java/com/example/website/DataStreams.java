package com.example.website;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class DataStreams extends DataStrings {
    static WordsBookRepository wordsBookRepository;
    String[][] in = {hymn, sobaka, vorona, chuchelo, rossia, pushkin, tutchev};

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

    static Set<String> readHagen(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file), Charset.forName("Cp1251"))) {
            Set<String> hagen = new HashSet<>();
            do {
                String line = reader.readLine();
                if (line.trim().isEmpty()) continue;
                hagen.add(line.split("\\|")[1].trim());
            } while (reader.ready());
            return hagen;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Set<String> readLopatin(String file) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(file))) {
            Set<String> lopatin = new HashSet<>();
            do {
                String line = reader.readLine()
                        .replaceAll("\\(.+\\)?", " ")
                        .replaceAll(".+#", "") // .replaceAll(".+%","")
                        .replaceAll(" [ивск] ", " ")
                        .replaceAll("союз|частица", "")
//                        .replaceAll(";.+", " ")
                        .toLowerCase();
                if (line.contains("часть сложных слов")) continue;
                if (line.contains("приставка")) continue;
                if (line.contains("пишется")) continue;
                if (line.contains("...")) continue;
//                if (line.contains("союз")) System.out.println(line);
//                if (line.contains("частица")) System.out.println(line);
                String[] words = line
                        .replaceAll("[а-я]+\\.", "")
                        .replaceAll("[^а-яё`'-]+", " ")
                        .split("\\s+");
//                System.out.println(stream(words).collect(Collectors.toList()));
                lopatin.addAll(wordExtend(words));
            } while (reader.ready());
            return lopatin;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<String> wordExtend(String[] words) {
        Set<String> wordSet = new HashSet<>();
        Set<String> wordSet2 = new HashSet<>();
        Set<String> ok = new HashSet<>();
        for (String word : words)
            if (word.matches("-.+")) ok.add(word); else wordSet.add(word);

        if (ok.size() > 0)
            for (String word : wordSet)
                for (String o : ok)
                    if (o.matches("-[ёуеыаоэяию`].*")) {
                        String w = join(word, o.replace("-", ""));
                        if (w.contains("+")) ; // System.out.println(word + " [" + o + "] " + w);
                        else wordSet2.add(w);
                    } else wordSet2.add(join2(word, o.replace("-", "")));

        wordSet.addAll(wordSet2);
//        System.out.println(wordSet);
        return wordSet;
    }

    private static String join2(String word, String o) {
        //        System.out.println(word + " [" + o + "] " + ws + o);
        return word.replaceFirst( o.charAt(0) + "{1}+.*$", o);
    }

    private static String join(String word, String o) {
        if (o.matches("`.+"))
            return word.replaceFirst("(.+)`.*", "$1") + o;
        else if (word.matches(".+[ёуеыаоэяию]$"))
            return word.replaceFirst("(.+)[ёуеыаоэяию]$", "$1") + o;
        else if (word.matches(".+ь$"))
            return word.replaceFirst("(.+)ь$", "$1") + o;
        else if (word.matches(".+й$"))
            if (o.contains("я")) return word + o;
            else if (word.contains("ный")) return word.replaceFirst("..ный", o);
            else if (word.contains("ий")) return word.replaceFirst("ий", o);
            else if (word.contains("ый")) return word.replaceFirst("ый", o);
            else if (word.contains("ей") && o.equals("и")) return word.replaceFirst("ей", o);
            else return word + " +" + o;
        return word + o;
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

    Set<String> extractWordSet(String file) throws IOException {
        return getTextStream(file).map(String::toLowerCase)
                .flatMap(l -> stream(l.replaceAll("[^а-яё`']+", " ").split("\\s+")))
                .filter(w -> w.contains("'") || w.contains("`"))
                .map(w -> w.replaceAll("`(.)", "$1'"))
                .collect(Collectors.toSet());
    }
}