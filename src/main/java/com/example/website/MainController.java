package com.example.website;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;

@Controller
public class MainController extends DataStreams {
    Sentence data = new Sentence("вихри враждебные веют над_нами");
    String texts = "texts/";

    List<File> textFilesExtra(String dir) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(dir))) {
            return stream.filter(file -> !Files.isDirectory(file))
                    .map(Path::toFile).collect(Collectors.toList());
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ModelAndView main(Model model) throws IOException {
        model.addAttribute("title", 0);
        model.addAttribute("files", textFilesExtra(texts));
        model.addAttribute("words", Dictionary.wordTable.size());
        return new ModelAndView("listfiles");
    }

    @GetMapping("/scan")
    @ResponseBody
    public ModelAndView scan(Model model) throws IOException {
        model.addAttribute("title", "SCANNER");
        Path thesaurus = Paths.get("thesaurus.txt");
        Files.write(thesaurus, Collections.singleton(""), UTF_8);
        for (File file : textFilesExtra(texts)) {
//            Files.write(thesaurus, Collections.singleton("\n<"+file.getPath()+">"), UTF_8,APPEND);
            Set<String> wordSet = extractWordSet(file.getAbsolutePath());
            Dictionary.addWordSet(wordSet);
            System.out.println(file + " : +" + wordSet.size()); // Dictionary.wordTable.size()
            Files.write(thesaurus, wordSet, UTF_8, APPEND);
        }
        return new ModelAndView("redirect:/");
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
        data = new Sentence(s);
        model.addAttribute("title", "COMBINER:" + data.words.length + "/" + data.amount);
        model.addAttribute("messages", data.fullOut());
        return new ModelAndView("page");
    }

    @GetMapping("/file/{file}")
    @ResponseBody
    public ModelAndView startPage(Model model, @PathVariable String file) throws IOException {
        Set<String> wordSet = extractWordSet(texts + file);
        Dictionary.addWordSet(wordSet);
//        System.out.println("Dictionary size:" + Dictionary.wordTable.size());
        List<String[]> text = getTextStream(texts + file)
                .map(s -> new Sentence(s).getHash(0)) // .randomOut(0)
                .collect(Collectors.toList());
        System.out.println(file + " #" + text.size());
        System.out.println(wordSet);
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