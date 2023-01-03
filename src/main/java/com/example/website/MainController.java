package com.example.website;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController extends DataStreams {
    WordsBookRepository repository = null;
    Combiner data = new Combiner("вихри враждебные веют над_нами", null);

    public MainController(WordsBookRepository repository) {
//        this.repository = repository;
//        System.out.println("ПОБЕДА");
//        words.stream().sorted().forEach(s -> System.out.print(s + ","));
//        System.out.println("победа");
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

    @GetMapping("/file/{file}")
    @ResponseBody
    public ModelAndView startPage(Model model, @PathVariable String file) {
        List<String[]> text = getText("texts/" + file + ".txt").stream()
                .map(s -> new Combiner(s, repository).getHash(0)) // .randomOut(0)
                .collect(Collectors.toList());
//        System.out.println("*" + text.size());
        model.addAttribute("messages", text);
//        model.addAttribute("title", "START:" + text.size());
        return new ModelAndView("pagehash");
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