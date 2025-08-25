package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.hero.service.HeroService;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import io.ethertale.findadicethymeleaf.web.dto.LoginDTO;
import io.ethertale.findadicethymeleaf.web.dto.RegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/")
public class MainController {

    private final UserService userService;
    private final HeroService heroService;

    @Autowired
    public MainController(UserService userService, HeroService heroService) {
        this.userService = userService;
        this.heroService = heroService;
    }

    @GetMapping("/")
    public ModelAndView home(){
        return new ModelAndView("welcomePage");
    }

    @GetMapping("/home")
    public ModelAndView homePage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){

        User loggedUser = userService.getUserById(authenticationDetails.getId());

        ModelAndView mav = new ModelAndView("home");
        mav.addObject("loggedUser", loggedUser);
        mav.addObject("loggedUserHero", loggedUser.getHero());

        return mav;
    }

    @GetMapping("/register")
    public ModelAndView register(){
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("registerDTO", new RegisterDTO());
        return mav;
    }

    @PostMapping("/register")
    public String registerProfile(RegisterDTO registerDTO){
        userService.registerUser(registerDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("loginDTO", new LoginDTO());
        return mav;
    }

    @GetMapping("/users")
    public ModelAndView usersListPage(){
        ModelAndView mav = new ModelAndView("usersList");
        mav.addObject("users", userService.getAllUsers());
        return mav;
    }

    @GetMapping("/characters")
    public ModelAndView charsListPage(){
        ModelAndView mav = new ModelAndView("charsList");
        mav.addObject("characters", heroService.getAllCharacters());
        return mav;
    }
}
