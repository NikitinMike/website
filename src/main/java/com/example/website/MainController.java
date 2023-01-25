package com.example.website;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Controller
public class MainController extends DataStreams {
    String source = "texts/";

    @GetMapping("/")
    @ResponseBody
    public ModelAndView main(Model model) throws IOException {
        model.addAttribute("title", 0);
        model.addAttribute("files", textFilesExtra(source));
        model.addAttribute("words", Dictionary.wordTable.size());
        return new ModelAndView("listfiles");
    }

    @GetMapping({"/dictionary/{from}", "/dictionary"})
    @ResponseBody
    public ModelAndView dictionary(Model model, @PathVariable @Nullable String from) {
        if (from == null || from.isEmpty()) from = "ё";
        model.addAttribute("title", from.toUpperCase());
        model.addAttribute("files", Collections.singleton(""));
        model.addAttribute("alphabet", "абвгдеёжзийклмнопрстуфхцчшщыэюя".split(""));
        char to = (char) (from.charAt(0) + 1);
        List<String> words = new TreeSet<>(Dictionary.wordTable.values())
                .subSet(from, Character.toString(to)).stream()
//                .map(s -> new Sentence(s).getHash(0)[1])// .randomOut(0)
                .map(s -> s.replaceAll("(.)'", "`$1"))
                .sorted(Comparator.comparing(Utils::reverse).reversed())
                .collect(Collectors.toList());
//                .collect(Collectors.toSet());
//                .collect(Collectors.joining(", "));
        model.addAttribute("words", words);
//        model.addAttribute("words", Collections.singleton(words));
        return new ModelAndView("dictionary");
    }

    @GetMapping({"/scan", "/scan/{page}"})
    @ResponseBody
    public ModelAndView scan(Model model, @PathVariable @Nullable String page) throws IOException {
        model.addAttribute("title", "SCANNER");
        Path thesaurus = Paths.get("thesaurus.txt");
        TreeSet<String> wordSet = new TreeSet<>(Comparator.comparing(Utils::reverse));
//        Files.write(thesaurus, Collections.singleton(""), UTF_8);
        for (File file : textFilesExtra(source)) {
//            Files.write(thesaurus, Collections.singleton("\n<"+file.getPath()+">"), UTF_8,APPEND);
            Set<String> set = thesaurusExtract(file.getAbsolutePath());
//            Set<String> set = extractWordSet(file.getAbsolutePath());
            Dictionary.addWordSet(set);
            System.out.println(file + " : +" + set.size()); // Dictionary.wordTable.size()
//            Files.write(thesaurus, set, UTF_8, APPEND);
            wordSet.addAll(set);
        }
        Files.write(thesaurus, wordSet, UTF_8);
//        System.out.println(page);
        if (page == null) return new ModelAndView("redirect:/");
        return new ModelAndView("redirect:/" + page.toLowerCase());
    }

    @GetMapping("/random")
    @ResponseBody
    public ModelAndView randomPageGet(Model model) {
        String s = hymn[(int) (hymn.length * Math.random())].replaceAll("[_,!.—]+", " ");
//        String s = vorona[(int) (vorona.length * Math.random())].replaceAll("_"," ");
//        String s = sobaka[(int) (sobaka.length * Math.random())];
//        String s = chuchelo[(int) (chuchelo.length * Math.random())];
        System.out.println();
        System.out.println(s);
        System.out.println("---------------------------------------------");
        Sentence data = new Sentence(s);
//    Sentence data = new Sentence("вихри враждебные веют над_нами");
        model.addAttribute("title", "COMBINER:" + data.words.length + "/" + data.amount);
        model.addAttribute("messages", data.fullOut());
        return new ModelAndView("page");
    }

    @GetMapping("/file/{file}")
    @ResponseBody
    public ModelAndView startPage(Model model, @PathVariable String file) throws IOException {
        Set<String> wordSet = extractWordSet(source + file);
        Dictionary.addWordSet(wordSet);
//        System.out.println("Dictionary size:" + Dictionary.wordTable.size());
        List<String[]> text = getTextStream(source + file)
                .map(s -> new Sentence(s).getHash(0)) // .randomOut(0)
                .collect(Collectors.toList());
        System.out.println(file + " #" + text.size());
//        System.out.println(wordSet);
        model.addAttribute("sentences", text);
//        model.addAttribute("title", "START:" + text.size());
        return new ModelAndView("text");
    }

    @GetMapping("/page/{i}")
    @ResponseBody
    public ModelAndView startPageGet(Model model, @PathVariable int i) {
        List<String> list = Arrays.stream(in[i % in.length])
                .map(s -> new Sentence(s).randomOut(1))
                .collect(Collectors.toList());
        model.addAttribute("messages", list);
        model.addAttribute("title", "START:" + list.size());
        return new ModelAndView("page");
    }
}
