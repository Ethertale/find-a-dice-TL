package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.event.service.EventService;
import io.ethertale.findadicethymeleaf.group.service.GroupService;
import io.ethertale.findadicethymeleaf.hero.service.HeroService;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final HeroService heroService;
    private final UserService userService;
    private final EventService eventService;
    private final GroupService groupService;

    @Autowired
    public SearchController(HeroService heroService, UserService userService, EventService eventService, GroupService groupService) {
        this.heroService = heroService;
        this.userService = userService;
        this.eventService = eventService;
        this.groupService = groupService;
    }

    @GetMapping
    public String search(@RequestParam(name = "search", required = false) String query, Model model) {
        if (query == null || query.isBlank()) {
            model.addAttribute("message", "Please enter a search term");
            model.addAttribute("query", "");
            return "searchResults";
        }

        var heroes = heroService.searchHeroes(query);
        var users = userService.searchUsers(query);
        var events = eventService.searchEvents(query);
        var groups = groupService.searchGroups(query);

        // Add results to the model
        model.addAttribute("query", query);
        model.addAttribute("heroes", heroes);
        model.addAttribute("users", users);
        model.addAttribute("events", events);
        model.addAttribute("groups", groups);

        return "searchResults";
    }
}
