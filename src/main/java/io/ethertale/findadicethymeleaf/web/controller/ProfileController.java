package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.hero.service.HeroService;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import io.ethertale.findadicethymeleaf.web.dto.HeroUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final HeroService heroService;

    @Autowired
    public ProfileController(UserService userService, HeroService heroService) {
        this.userService = userService;
        this.heroService = heroService;
    }

    @GetMapping("/{id}")
    public ModelAndView profile(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @PathVariable UUID id) {
        ModelAndView mav = new ModelAndView("profile");

        User loggedUser = userService.getUserById(authenticationDetails.getId());

        mav.addObject("loggedUser", loggedUser);
        mav.addObject("user", userService.getUserById(id));
        mav.addObject("loggedUserHero", loggedUser.getHero());

        return mav;
    }

    @GetMapping("/{id}/edit-hero")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public ModelAndView editHero(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @PathVariable UUID id) {

        User loggedUser = userService.getUserById(authenticationDetails.getId());

        ModelAndView mav = new ModelAndView("editHero");
        mav.addObject("editHeroDTO", new HeroUpdateDTO());
        mav.addObject("loggedUser", loggedUser);
        mav.addObject("user", userService.getUserById(id));
        mav.addObject("genders", heroService.getAllGenders());
        mav.addObject("charClasses", heroService.getAllCharClasses());
        mav.addObject("alignments", heroService.getAllAlignments());
        mav.addObject("backgrounds", heroService.getAllBackgrounds());
        mav.addObject("races", heroService.getAllRaces());

        return mav;
    }

    @PostMapping("/{id}/edit-hero/success")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public String editHeroSuccess(HeroUpdateDTO heroUpdateDTO, @AuthenticationPrincipal AuthenticationDetails authenticationDetails, @PathVariable UUID id) {
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        heroService.updateHero(loggedUser.getHero(), heroUpdateDTO);
        return "redirect:/profile/ " + id;
    }
}
