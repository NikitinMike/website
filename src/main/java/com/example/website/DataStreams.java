package com.example.website;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.newBufferedReader;
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
        Set<String> wordSet = new HashSet<>();
        if (new File(file).isFile())
            try (BufferedReader reader = newBufferedReader(Path.of(file))) {
                do {
                    String[] lines = reader.readLine().split("#");
                    Set<String> set = stream(lines[1].split(","))
//                        .filter(w -> !w.equals("-"))
                            .collect(Collectors.toSet());
                    if (lines[1].contains(",-"))
                        for (String w : set)
                            if (w.matches("-.+"))
                                if (lines[0].matches(".+очьс?я?"))
                                    wordSet.add(lines[0].replaceFirst("очьс?я?", w));
                                else System.out.println(lines[0] + w);
                            else wordSet.add(w);
                    else wordSet.addAll(set);
                } while (reader.ready());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return wordSet;
    }

    static Set<String> readHagen(String file) {
        Set<String> hagen = new HashSet<>();
        if (new File(file).isFile())
            try (BufferedReader reader = newBufferedReader(Path.of(file), forName("Cp1251"))) {
                do {
                    String line = reader.readLine();
                    if (line.trim().isEmpty()) continue;
                    hagen.add(line.split("\\|")[1].trim());
                } while (reader.ready());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return hagen;
    }

    static Set<String> readLopatin(String file) {
        Set<String> lopatin = new HashSet<>();
        if (new File(file).isFile())
            try (BufferedReader reader = newBufferedReader(Path.of(file))) {
                do {
                    String line = reader.readLine().toLowerCase()
                            .replaceAll("\\(.+\\)?", " ")
                            .replaceAll(".+#", "") // .replaceAll(".+%","")
                            .replaceAll(" [ивск] ", " ")
                            .replaceAll("союз|частица", "")
                            .replaceAll("[а-я]+\\.", "")
//                        .replaceAll(";.+", " ")
                            ;
                    if (line.contains("часть сложных слов")) continue;
                    if (line.contains("приставка")) continue;
                    if (line.contains("пишется")) continue;
                    if (line.contains("...")) continue;
                    String words = line.replaceAll("[^а-яё`'-]+", " ");
                    lopatin.addAll(wordExtend(words.split("\\s+")));
                } while (reader.ready());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return lopatin;
    }

    private static Set<String> wordExtend(String[] words) {
        Set<String> wordSet = new HashSet<>();
        Set<String> wordSetExt = new HashSet<>();
        Set<String> ok = new HashSet<>();
        for (String word : words)
            if (word.matches("-.+")) ok.add(word);
            else if (!word.matches(".*-") && word.contains("`")) wordSet.add(word);
        if (ok.size() > 0)
            for (String word : wordSet)
                for (String o : ok)
                    if (o.matches("-[ёуеыаоэяию`].*")) {
                        String w = join(word, o.replace("-", ""));
                        if (w.contains("+")) System.out.println(word + " [" + o + "] " + w);
                        else wordSetExt.add(w);
                    } else wordSetExt.add(joinOk(word, o.replace("-", "")));
        wordSet.addAll(wordSetExt);
        return wordSet;
    }

    private static String joinOk(String word, String o) {
        int l = word.lastIndexOf(o.charAt(0));
        if (l < 0) return word;
        String sl = word.substring(l);
        if(sl.contains("`")) o=o.replaceFirst("([ёуеыаоэяию])","`$1");
        String w = word.replaceFirst(sl, o).replaceFirst("``","`");
        if (!w.contains("`") && !w.contains("ё")) System.out.printf("%s [%s] %s %n ", word, o, w);
        return w;
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
            else if (word.contains("ей") && o.matches("и|а|ю|е.")) return word.replaceFirst("ей", o);
            else if (word.contains("ой") && o.matches("а|ю|ы|е.|ёв")) return word.replaceFirst("ой", o);
            else if (word.contains("ай") && o.matches("ю")) return word.replaceFirst("ай", o);
            else return word + " +" + o;
        return word + o;
    }

    static Set<String> readThesaurus(String file) {
        Set<String> wordSet = new HashSet<>();
        if (new File(file).isFile())
            try (BufferedReader reader = newBufferedReader(Path.of(file))) {
                do {
                    Collections.addAll(wordSet, reader.readLine().split(",")); // .replaceAll("[^а-яёА-ЯЁ`',]","")
                } while (reader.ready());
                return wordSet;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return wordSet;
    }

    static Stream<String> getTextStream(String fileName) throws IOException {
        return newBufferedReader(Path.of(fileName)).lines().map(String::trim);
    }

    List<File> textFilesExtra(String dir) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(dir))) {
            return stream.filter(file -> !Files.isDirectory(file))
                    .map(Path::toFile).collect(Collectors.toList());
        }
    }

    Set<String> extractWordSet(String file) throws IOException {
        if (new File(file).isFile())
            return getTextStream(file).map(String::toLowerCase)
                    .flatMap(l -> stream(l.replaceAll("[^а-яё`']+", " ").split("\\s+")))
                    .filter(w -> w.contains("'") || w.contains("`"))
                    .map(w -> w.replaceAll("`(.)", "$1'"))
                    .collect(Collectors.toSet());
        return null;
    }

    Set<String> thesaurusExtract(String file) throws IOException {
        if (new File(file).isFile())
            return getTextStream(file).map(String::toLowerCase)
                    .flatMap(l -> stream(l.replaceAll("[^а-яё`']+", " ").split("\\s+")))
                    .map(Dictionary::getWord)
                    .filter(w -> w.contains("'") || w.contains("`") || w.contains("ё"))
                    .map(w -> w.replaceAll("`(.)", "$1'"))
                    .filter(w -> w.replaceAll("[ёуеыаоэяию]", "").length() + 1 < w.length())
                    .sorted(Comparator.comparing(Utils::reverse))
//                .map(w-> new Sentence(w).getHash(0)[1])
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        else return null;
    }
}