package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.hero.model.Classes;
import io.ethertale.findadicethymeleaf.hero.service.HeroService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/class-guides")
public class GuidesController {

    private final HeroService heroService;

    public GuidesController(HeroService heroService) {
        this.heroService = heroService;
    }

    @GetMapping
    public ModelAndView classesPage() {
        ModelAndView modelAndView = new ModelAndView("classes");
        modelAndView.addObject("classes", Classes.values());
        return modelAndView;
    }
}
