package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.deletedReport.model.DeletedReportType;
import io.ethertale.findadicethymeleaf.deletedReport.repo.DeletedReportRepo;
import io.ethertale.findadicethymeleaf.deletedReport.service.DeletedReportService;
import io.ethertale.findadicethymeleaf.event.service.EventService;
import io.ethertale.findadicethymeleaf.group.service.GroupService;
import io.ethertale.findadicethymeleaf.hero.service.HeroService;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.model.UserRoles;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/")
public class MainController {

    private final UserService userService;
    private final HeroService heroService;

    private final DeletedReportService deletedReportService;

    @Autowired
    public MainController(UserService userService, HeroService heroService, DeletedReportService deletedReportService) {
        this.userService = userService;
        this.heroService = heroService;
        this.deletedReportService = deletedReportService;
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
    public ModelAndView login(@RequestParam(value = "error", required = false) String errorParam){
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("loginDTO", new LoginDTO());

        if (errorParam != null) {
            mav.addObject("errorMessage", "Incorrect username or password!");

        }
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

    @GetMapping("/documentation")
    public ModelAndView documentationPage(){
        return new ModelAndView("documentation");
    }

    @GetMapping("/dice-roll")
    public ModelAndView diceRollPage(){return new ModelAndView("diceRolls");}

    @GetMapping("/admin-panel")
    public ModelAndView adminPanelPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        ModelAndView mav = new ModelAndView("adminPanel");
        mav.addObject("deletedGroupsReports", deletedReportService.getAllDeletedReportsFromType(DeletedReportType.GROUP));
        mav.addObject("deletedGroupPostsReports", deletedReportService.getAllDeletedReportsFromType(DeletedReportType.GROUP_POST));
        mav.addObject("deletedEventsReports", deletedReportService.getAllDeletedReportsFromType(DeletedReportType.EVENT));
        mav.addObject("deletedCampaignChatMessagesReports", deletedReportService.getAllDeletedReportsFromType(DeletedReportType.CAMPAIGN_CHAT_MESSAGE));
        mav.addObject("deletedChatroomMessagesReports", deletedReportService.getAllDeletedReportsFromType(DeletedReportType.CHAT_MESSAGE));

        return mav;
    }
}
