package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.campaign.model.Campaign;
import io.ethertale.findadicethymeleaf.campaign.model.CampaignMembership;
import io.ethertale.findadicethymeleaf.campaign.model.MembershipStatus;
import io.ethertale.findadicethymeleaf.campaign.service.CampaignService;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import io.ethertale.findadicethymeleaf.web.dto.campaign.CampaignCreateDTO;
import io.ethertale.findadicethymeleaf.web.dto.campaign.CharacterSheetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/campaigns")
public class CampaignController {

    private final CampaignService campaignService;
    private final UserService userService;

    @Autowired
    public CampaignController(CampaignService campaignService, UserService userService) {
        this.campaignService = campaignService;
        this.userService = userService;
    }

    // Listing
    @GetMapping
    public ModelAndView campaigns(){
        ModelAndView mav = new ModelAndView("campaigns");
        mav.addObject("campaigns", campaignService.getAllActiveCampaignsSortedByCreation());
        return mav;
    }

    // Create
    @GetMapping("/create")
    public ModelAndView createCampaignPage(){
        ModelAndView mav = new ModelAndView("campaignCreate");
        mav.addObject("campaignDTO", new CampaignCreateDTO());
        return mav;
    }

    @PostMapping("/create")
    public String createCampaign(@ModelAttribute("campaignDTO") CampaignCreateDTO campaignDTO, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());
        Campaign campaign = campaignService.createCampaign(campaignDTO, loggedUser.getHero());

        return "redirect:/campaigns/" + campaign.getId();
    }
}
