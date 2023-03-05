package com.example.website;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Utils {

    static int[] f = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880}; // 0!-9!

    static int factorial(int n) {
        if (n < 10) return f[n];
        return factorial(n - 1) * n;
    }

    public static String formatWordType(String word, String type) {
        if (type == null) type = "";
        else switch (type) {

                case "сущ": return "<i class=noun>" + wordAnalyse(word) + "</i>";

                case "прл": return "<i class=adj>" + wordAnalyse(word) + "</i>";
                case "числ": return "<b class=adj>" + wordAnalyse(word) + "</b>";

                case "гл": return "<i class=verb>" + wordAnalyse(word) + "</i>";
                case "нар": return "<b class=verb>" + wordAnalyse(word) + "</b>";
                case "ввод": return "<i class=verb><s>" + wordAnalyse(word) + "</s></i>";

                case "межд": return "<i class=union>" + wordAnalyse(word) + "</i>";
                case "союз": return "<b class=union>" + wordAnalyse(word) + "</b>";
                case "предл": return "<b class=prep>" + wordAnalyse(word) + "</b>";
                case "част": return "<b class=art>" + wordAnalyse(word) + "</b>";

                case "прч": return "<i class=part>" + wordAnalyse(word) + "</i>";
                case "предик": return "<i class=part><s>" + wordAnalyse(word) + "</s></i>";
                case "дееп": return "<b class=part>" + wordAnalyse(word) + "</b>";

                default:
                    if(type.contains("мест"))
                        return "<b class=pron>" + wordAnalyse(word) + "</b>";
//                    case "сущ,мест": return "<b class='noun'>" + wordAnalyse(word) + "</b>";
//                    case "прл,мест": return "<b class='adj'>" + wordAnalyse(word) + "</b>";
//                    case "нар,мест": return "<b class='verb'>" + wordAnalyse(word) + "</b>";
            }
        return "<a href='/'>"+wordAnalyse(word) + (type.isEmpty()?"":":") + type+"</a>";
    }

    List<String> getResourceFiles() throws IOException {
        return Arrays.stream(Objects.requireNonNull(
                new ClassPathResource("/texts").getFile().listFiles()
        )).map(File::getName).collect(Collectors.toList());
    }

    List<String> getResourceTextFile(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            assert inputStream != null;
            return new BufferedReader(new InputStreamReader(inputStream, UTF_8)).lines()
                    // .map(s -> s.replaceAll("[-\"'_,!.—?:;\\d\\s]+", " ")
                    .map(s -> s.replaceAll("[^а-яА-ЯёЁ`']+", " ")
                    .trim()).filter(p -> !p.isEmpty()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }

//    public static <T> void swap(T[] a, int i, int j) {T t = a[i];a[i] = a[j];a[j] = t;}
//    public static void hackSwap(int[] arr, int i, int j) {
//        arr[i] = (arr[i] + arr[j]) - (arr[j] = arr[i]);
//    }

    public static int[] swapClone(int[] in, int a) {
        int[] out = in.clone();
        int b = a - 1;
        int c = out[b];
        out[b] = out[a];
        out[a] = c;
        return out;
    }

    static String tag(String word) {
        return word.trim().toLowerCase()
                .replaceAll("`(.)", "$1'") // from lopatin's wordbook
                .replaceAll("([ёуеыаоэяию])", "$1=")
                .replaceAll("=([^ёуеыаоэяию]+)$", "$1")
                .replaceAll("(=[^ёуеыаоэяию]*)([^ёуеыаоэяию])", "$1=$2")
                .replaceAll("=([^ёуеыаоэяию]+)=", "$1=")
                .replaceAll("=([ьъ])", "$1=")
//                .replaceAll("(.)'","`$1")
//                .replaceAll("`(.)","$1")
//                .replaceAll("`=","=`")
//                .replaceAll("-'","'-")
//                .replaceAll("-","|")
                .replaceAll("=+'", "'=")
//                .replaceAll("[ёуеыаоэяию]","@")
                ;
    }

    static int glasCount(String word){
        return word.replaceAll("[^ёуеыаоэяию]+", "").length();
//        return getGlas(word.replaceAll("'`","")).length();
//        int count=0;
//        for (Byte b : word.getBytes()) if ("ёуеыаоэяию".contains(b.toString())) count++;
//        return count;
    }

    public static String getGlas(String word) {
//        return word.replaceAll("[йцкнгшщзхъфвпрлджчсмтьб]", "");
        return word.replaceAll("[^ёуеыаоэяию`']", "");
    }

    public static String wordStrip(String word) {
        return Arrays.stream(tag(word).split("=+"))
                .map(s -> s.matches(".'|`.") ? s.toUpperCase() : s) // new Locale("en", "EN")
                .collect(Collectors.joining("-"))
                .replaceAll("'", "");
    }

    public static String wordAnalyse(String word) {
        return String.join("-", tag(word).split("=+"));
    }

    public static String[] wordsExpander(List<WordBookEntity> words) {
        StringJoiner full = new StringJoiner(" ");
        StringJoiner strip = new StringJoiner("");
        for (WordBookEntity wordBookEntity : words) {
            String word = wordBookEntity.getWordType();
            full.add(word.replaceAll("(.)'", "`$1"));
            strip.add(word.replaceAll("[бвгджзйклмнпрстфхцчшщьъ-]", ""));
        }
        return new String[]{full.toString(), strip.toString()};
    }
}