package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private final GroupService groupService;

    @Autowired
    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ModelAndView groups() {
        ModelAndView modelAndView = new ModelAndView("groups");
        modelAndView.addObject("groups", groupService.getAllGroupsSortedByCreationDesc());
        return modelAndView;
    }
}
