package com.example.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.website.Utils.glasCount;
import static com.example.website.Utils.reverse;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.newBufferedReader;
import static java.util.Arrays.stream;

@Service
public class DataStreams extends DataStrings {
    @Autowired
    WordsBookRepository wordsBookRepository;
    String[][] in = {hymn, sobaka, vorona, chuchelo, rossia, pushkin, tutchev};

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
                    String[] record = reader.readLine().replace("...", "").toLowerCase().split("[#%]", 3);
                    if (record.length < 3) continue; // bad line
                    if (record[0].matches("^-.+")) continue;
                    if (record[2].contains("часть сложных слов")) continue;
                    if (record[2].contains("приставка")) continue;
                    if (record[2].contains("пишется")) continue;
//                    if (record[2].contains(record[1]+"...")) continue;
                    lopatin.addAll(List.of(record[1].split("[-\\s]"))); // split??? and add main words
                    String line = record[2]
                            .replaceAll("\\[.+\\]", " ")
                            .replaceAll("\\(.+\\)?", " ")
//                            .replaceAll(".+#", "") // .replaceAll(".+%","")
                            .replaceAll(" [ивск] ", " ")
                            .replaceAll("союз|частица", "")
                            .replaceAll("[а-я]+\\.", "")
                            .replace(" тв ", " ")
                            .replace(" мн ", " ")
                            .replaceAll(":.+", "")
//                        .replaceAll(";.+", " ")
                            .replaceAll("[^а-яё`'-]+", " ");
                    lopatin.addAll(wordExtend(line.split("\\s+"))); // extend word with some ends
                } while (reader.ready());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return lopatin;
    }

    private static Set<String> wordExtend(String[] words) {
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

    @Transactional
    List<WordBookEntity> readWordBook(List<String> words) {
//        Hashtable<String, WordBookEntity> wordBookEntities = new Hashtable<>();
        List<WordBookEntity> wordBookEntities = new ArrayList<>();
        for (String word : words) {
//            String [] subWords = word.split("_");
//            if(subWords.length>1) wordsEntity.addAll(readWordsBook(subWords));
//            else wordsEntity.addAll(repository.findAllByWord(word));
            word = word.replaceAll("[^а-яА-ЯёЁ]", "");
            List<WordBookEntity> entities = wordsBookRepository.findFirstByWord(word);
            if (entities == null || entities.isEmpty())
                wordBookEntities.add(new WordBookEntity(word));
            else wordBookEntities.addAll(entities);
//            System.out.println(entities);
        }
//        wordBookEntities.sort(Comparator.comparing(WordBookEntity::getType));
        return wordBookEntities;
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