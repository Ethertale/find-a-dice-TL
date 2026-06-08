package io.ethertale.findadicethymeleaf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/guides")
public class GuidesController {

//    @GetMapping("/classes")
//    public ModelAndView classesPage() {
//        ModelAndView modelAndView = new ModelAndView("classes");
//        modelAndView.addObject("classes", Classes.values());
//        return modelAndView;
//    }

    @GetMapping("/how-to-create-your-first-character")
    public ModelAndView racesPage(){
        return new ModelAndView("firstCharGuide");
    }
}
