package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.event.service.EventService;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import io.ethertale.findadicethymeleaf.web.dto.EventCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/events")
public class EventsController {

    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public EventsController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView events(){
        ModelAndView modelAndView = new ModelAndView("events");
        modelAndView.addObject("events", eventService.getAllEventsSortedByCreationDesc());
        return modelAndView;
    }

    @GetMapping("/create-event")
    public ModelAndView createEvent(){
        ModelAndView modelAndView = new ModelAndView("eventCreate");
        modelAndView.addObject("eventDTO", new EventCreateDTO());
        return modelAndView;
    }

    @PostMapping("/create-event/create")
    public String createEvent(@ModelAttribute("eventDTO") EventCreateDTO eventDTO, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        eventService.createEvent(eventDTO, loggedUser.getHero());
        return "redirect:/events";
    }

    @GetMapping("/{id}")
    public ModelAndView event(@PathVariable("id") UUID eventId, @AuthenticationPrincipal AuthenticationDetails authenticationDetails, Model model){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        ModelAndView modelAndView = new ModelAndView("eventView");
        modelAndView.addObject("event", eventService.getSpecificGroup(eventId));
        modelAndView.addObject("loggedUser", loggedUser);

        return modelAndView;
    }
}
