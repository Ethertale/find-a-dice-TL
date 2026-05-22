package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.campaign.service.CampaignService;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
