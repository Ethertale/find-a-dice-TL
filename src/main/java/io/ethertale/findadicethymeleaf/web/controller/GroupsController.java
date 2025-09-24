package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.group.service.GroupService;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import io.ethertale.findadicethymeleaf.web.dto.GroupCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/create-group")
    public ModelAndView createGroup() {
        ModelAndView modelAndView = new ModelAndView("groupCreate");

        modelAndView.addObject("groupDTO", new GroupCreateDTO());
        return modelAndView;
    }

    @PostMapping("/create-group/create")
    public String createGroup(@ModelAttribute("groupDTO") GroupCreateDTO groupDTO, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        groupService.createGroup(groupDTO, loggedUser.getHero());
        return "redirect:/groups";
    }

    @GetMapping("/{id}")
    public ModelAndView group(@PathVariable("id") UUID groupId, @AuthenticationPrincipal AuthenticationDetails authenticationDetails, Model model) {
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        ModelAndView modelAndView = new ModelAndView("groupView");
        modelAndView.addObject("group", groupService.getSpecificGroup(groupId));
        modelAndView.addObject("loggedUser", loggedUser);
        return modelAndView;
    }

    @PostMapping("/{id}/join-group-{heroId}")
    public String joinGroup(@PathVariable("id") UUID groupId, @PathVariable("heroId") UUID loggedUserHeroId, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        groupService.addHeroToGroup(loggedUserHeroId, groupId);
        return "redirect:/groups/" + groupId;
    }

    @PostMapping("/{id}/leave-group-{heroId}")
    public String leaveGroup(@PathVariable("id") UUID groupId, @PathVariable("heroId") UUID loggedUserHeroId, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        groupService.removeHeroFromGroup(loggedUserHeroId, groupId);
        return "redirect:/groups/" + groupId;
    }
}
