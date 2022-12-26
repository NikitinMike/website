package com.example.website;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MainController extends DataStreams {
    static final Set<String> words = readWordBookFile(new File("D:\\DBWords\\wordbook.txt"));
    WordsBookRepository repository=null;
    Combiner data = new Combiner("вихри враждебные веют над_нами", null);

    public MainController(WordsBookRepository repository) {
        this.repository = repository;
        System.out.println("ПОБЕДА");
//        for (String word : words) System.out.print(word+",");
//        words.stream().sorted().forEach(s -> System.out.print(s + ","));
        System.out.println("победа");
        System.out.printf("Wordbook %s words%n", words.size());
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
        data = new Combiner(s, repository);
        model.addAttribute("title", "COMBINER:" + data.words.length + "/" + data.amount);
        model.addAttribute("messages", data.fullOut());
        return new ModelAndView("page");
    }

    @GetMapping("/")
    @ResponseBody
    public ModelAndView startPage(Model model) {
        List<String> text = getText("Pushkin.txt").stream()
                .map(s -> new Combiner(s, repository).randomOut(0))
                .collect(Collectors.toList());
//        list.stream().map(Utils::wordSplit).forEach(System.out::println);
        System.out.println("*" + text.size());
        model.addAttribute("messages", text);
        model.addAttribute("title", "START:" + text.size());
        return new ModelAndView("page");
    }

    @GetMapping("/{i}")
    @ResponseBody
    public ModelAndView startPageGet(Model model, @PathVariable int i) {
        List<String> list = Arrays.stream(in[i % 4])
                .map(s -> new Combiner(s.replaceAll("[_,!.—?;:']+", " "), repository).randomOut(1))
                .collect(Collectors.toList());
        model.addAttribute("messages", list);
        model.addAttribute("title", "START:" + list.size());
        return new ModelAndView("page");
    }
}