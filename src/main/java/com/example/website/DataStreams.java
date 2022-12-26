package com.example.website;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

public class DataStreams {
    String[] hymn = {
            "Братских народов союз вековой,",
            "Грядущие нам открывают года.",
            "Могучая воля, великая слава —",
            "Нам силу даёт наша верность Отчизне.",
            "Одна ты на свете! Одна ты такая —",
            "От южных морей до полярного края",
            "Предками данная мудрость народная!",
            "Раскинулись наши леса и поля.",
            "Россия — любимая наша страна.",
            "Россия — священная наша держава,",
            "Славься, Отечество наше свободное,",
            "Славься, страна! Мы гордимся тобой!",
            "Так было, так есть и так будет всегда!",
            "Твоё достоянье на все времена!",
            "Хранимая Богом родная земля!",
            "Широкий простор для мечты и для жизни",
    };
    String[] sobaka = {
            "у_попа была собака",
            "Он ее любил",
            "Она съела кусок мяса",
            "Он ее убил",
            "И в землю закопал",
            "И надпись написал",
            "у_попа была собака",
            "Он ее любил"
    };
    String[] vorona = {
            "Шел я как-то через_мост",
            "Глядь ворона сохнет",
            "Взял ворону я за_хвост",
            "Положил ее под_мост",
            "Пусть ворона мокнет",
            "Снова шел я через_мост",
            "Глядь ворона мокнет",
            "Снова взял ее за_хвост",
            "Положил ее на_мост",
            "Пусть ворона сохнет"
    };
    String[] chuchelo = {
            "Чучело-мяучело на_трубе сидело",
            "Чучело-мяучело песенку запело",
            "Только всем от_песенки_этой стало тошно",
            "Потому_что песенка чучела про_то_что",
            "Чучело-мяучело на_трубе сидело"
    };

    String[][] in = {hymn, sobaka, vorona, chuchelo};

    static Hashtable<String, List<WordsBookEntity>> readWordsBookDB(WordsBookRepository repository, String[] words) {
        Hashtable<String, List<WordsBookEntity>> wordsEntityHashMap = new Hashtable<>();
//        return Arrays.stream(words).flatMap(word -> repository.findAllByWord(word).stream()).collect(Collectors.toList());
        for (String word : words) {
//            String [] subWords = word.split("_");
//            if(subWords.length>1) wordsEntity.addAll(readWordsBook(subWords));
//            else wordsEntity.addAll(repository.findAllByWord(word));
            List<WordsBookEntity> wordsBookEntities = repository.findAllByWord(word);
            if (wordsBookEntities == null || wordsBookEntities.isEmpty())
                wordsBookEntities = Collections.singletonList(new WordsBookEntity(word));
//            System.out.println(wordsBookEntities);
//            WordsBookEntity [] wordBook = (WordsBookEntity[]) wordsBookEntities.toArray();
            wordsEntityHashMap.put(word, wordsBookEntities);
        }
//        for (WordsBookEntity word : wordsEntityList) System.out.println(word);
        //            wordsEntityHashMap.put(word.getWord(),word);
        return wordsEntityHashMap;
//        return null;
    }

    static Set<String> readWordBookFile(File fileIn) {
        try (BufferedReader reader = Files.newBufferedReader(fileIn.toPath())) {
//            long l = 0, ll = 0, ls = 0, w = 0;
            Set<String> wordSet = new HashSet<>();
            do {
                String line = reader.readLine();
//                l += line.length();
                String[] lines = line.split("#");
//                System.out.print(lines[0]+",");
                String[] words = lines[1]
//                        .replaceAll("[^ёуеыаоэяию,']","")
                        .split(",");
//                Arrays.stream(words).forEach(System.out::print);
//                System.out.println(String.join(",", words));
//                w += words.length;
                List<String> list = asList(words);
//                ll += list.size();
                Set<String> set = new HashSet<>(list);
//                System.out.println(set);
//                ls += set.size();
                wordSet.addAll(set);
            } while (reader.ready());
//            System.out.println(l);
//            System.out.println(w);
//            System.out.println(ll);
//            System.out.println(ls);
            return wordSet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    List<String> getText(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            assert inputStream != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
            return reader.lines() // .map(s -> s.replaceAll("[-\"'_,!.—?:;\\d\\s]+", " ")
                    .map(s -> s.replaceAll("[^а-яА-ЯёЁ]+", " ")
                            .trim()).filter(p -> !p.isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
