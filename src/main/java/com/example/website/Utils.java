package com.example.website;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Utils {

    static int[] f = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880}; // 0!-9!

    static int factorial(int n) {
        if (n < 10) return f[n];
        return factorial(n - 1) * n;
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
        return word.replaceAll("([^ёуеыаоэяию'])", "").length();
//        int count=0;
//        for (Byte b : word.getBytes()) if ("ёуеыаоэяию".contains(b.toString())) count++;
//        return count;
    }

    public static String getGlas(String word) {
        return word.replaceAll("[йцкнгшщзхъфвпрлджчсмтьб]", "");
    }

    public static String wordStrip(String word) {
        return Arrays.stream(tag(word).split("=+"))
                .map(s -> s.matches(".'|`.") ? s.toUpperCase() : s) // new Locale("en", "EN")
                .collect(Collectors.joining("-"))
                .replaceAll("'", "");
    }

    public static String wordAnalyse(String word) {
        return String.join("-", tag(word).split("=+")); // .replaceAll("(.)'","`$1");
    }
}