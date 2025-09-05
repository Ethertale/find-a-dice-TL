package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.group.service.GroupService;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public GroupsController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView groups() {
        ModelAndView modelAndView = new ModelAndView("groups");
        modelAndView.addObject("groups", groupService.getAllGroupsSortedByCreationDesc());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView group(@PathVariable("id") UUID groupId, @AuthenticationPrincipal AuthenticationDetails authenticationDetails, Model model) {
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        ModelAndView modelAndView = new ModelAndView("groupView");
        modelAndView.addObject("group", groupService.getSpecificGroup(groupId));
        modelAndView.addObject("loggedUser", loggedUser);
        return modelAndView;
    }
}
