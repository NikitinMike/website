package com.example.website;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {

    static int[] f = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880}; // 0!-9!

    static int factorial(int n) {
        if (n < 10) return f[n];
        return factorial(n - 1) * n;
    }

    public static String notNull(String tag, String s) {
        return s == null ? "" : String.format(",%s='%s'", tag, s);
    }

    public static String notNull(String tag, Long c) {
        return (c == null || 0 == c) ? "" : String.format(",%s=%d", tag, c);
    }

    public static String notNull(String tag, Byte b) {
        return (b == null || 0 == b) ? "" : String.format(",%s=%d", tag, b);
    }

    public static <T> void swap(T[] a, int i, int j) {
        T t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void hackSwap(int[] arr, int i, int j) {
        arr[i] = (arr[i] + arr[j]) - (arr[j] = arr[i]);
    }

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

    public static String wordStrip(String word) {
        return Arrays.stream(tag(word).replaceAll("[йцкнгшщзхъфвпрлджчсмтьб]", "")
                        .split("=+"))
                .map(s -> s.matches(".'|`.") ? s.toUpperCase() : s) // new Locale("en", "EN")
                .collect(Collectors.joining("-"))
                .replaceAll("'", "");
    }

    public static String wordAnalyse(String word) {
        return String.join("-", tag(word).split("=+"));
    }
}