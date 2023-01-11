package com.example.website;

import org.springframework.core.io.ClassPathResource;
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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MainController extends DataStreams {
    WordsBookRepository repository = null;
    Sentence data = new Sentence("вихри враждебные веют над_нами", null);

    public MainController(WordsBookRepository repository) {
//        this.repository = repository;
//        System.out.println("ПОБЕДА");
//        words.stream().sorted().forEach(s -> System.out.print(s + ","));
//        System.out.println("победа");
    }

    List<String> fileResources() throws IOException {
        return Arrays.stream(Objects.requireNonNull(
                new ClassPathResource("/texts").getFile().listFiles()
        )).map(File::getName).collect(Collectors.toList());
    }

    List<File> textFilesExtra() throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get("texts/"))) {
            return stream.filter(file -> !Files.isDirectory(file))
                    .map(Path::toFile).collect(Collectors.toList());
        }
    }

    @GetMapping("/")
    @ResponseBody
    public ModelAndView main(Model model) throws IOException {
        model.addAttribute("title", 0);
        model.addAttribute("files", textFilesExtra());
        return new ModelAndView("listfiles");
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
        data = new Sentence(s, repository);
        model.addAttribute("title", "COMBINER:" + data.words.length + "/" + data.amount);
        model.addAttribute("messages", data.fullOut());
        return new ModelAndView("page");
    }

    @GetMapping("/file/{file}")
    @ResponseBody
    public ModelAndView startPage(Model model, @PathVariable String file) throws IOException {
        System.out.println(file);

        Set<String> wordSet = readTextWordSet("texts/" + file);
        System.out.println(wordSet);
        Dictionary.addWordSet(wordSet);
        System.out.println("Dictionary size:"+Dictionary.wordTable.size());

        List<String[]> text = getTextStream("texts/" + file)
                .map(s -> new Sentence(s, repository).getHash(0)) // .randomOut(0)
                .collect(Collectors.toList());
        System.out.println(" #" + text.size());
        model.addAttribute("sentences", text);
//        model.addAttribute("title", "START:" + text.size());
        return new ModelAndView("text");
    }

    @GetMapping("/page/{i}")
    @ResponseBody
    public ModelAndView startPageGet(Model model, @PathVariable int i) {
        List<String> list = Arrays.stream(in[i % in.length])
                .map(s -> new Sentence(s, repository).randomOut(1))
                .collect(Collectors.toList());
        model.addAttribute("messages", list);
        model.addAttribute("title", "START:" + list.size());
        return new ModelAndView("page");
    }
}