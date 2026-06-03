package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.campaign.service.CampaignService;
import io.ethertale.findadicethymeleaf.config.Utils;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import io.ethertale.findadicethymeleaf.web.dto.campaign.WSCampaignIncomingMessageDTO;
import io.ethertale.findadicethymeleaf.web.dto.campaign.WSCampaignOutgoingMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
public class CampaignWebsocketController {

    private final CampaignService campaignService;
    private final UserService userService;
    private final Utils utils;

    @Autowired
    public CampaignWebsocketController(CampaignService campaignService, UserService userService, Utils utils) {
        this.campaignService = campaignService;
        this.userService = userService;
        this.utils = utils;
    }


    /**
     * Handles incoming chat messages from campaign participants.
     *
     * Browser sends to:   /app/campaign/{id}/chat
     * Server broadcasts:  /topic/campaign/{id}/chat
     *
     * Only the DM or active members should be on this page,
     * so we trust the session. No extra role check needed here.
     * The controller-level guards already prevent others from
     * even loading the campaign view.
     */
    @MessageMapping("/campaign/{campaignId}/chat")
    @SendTo("/topic/campaign/{campaignId}/chat")
    public WSCampaignOutgoingMessageDTO handleCampaignMessage(@DestinationVariable String campaignId, WSCampaignIncomingMessageDTO incomingMessage, Principal principal) {

        // Unwrap the Principal
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
        AuthenticationDetails authDetails = (AuthenticationDetails) authToken.getPrincipal();
        User loggedUser = userService.getUserById(authDetails.getId());

        // Persist the message
        campaignService.saveMessage(UUID.fromString(campaignId), loggedUser.getHero(), incomingMessage.getMessage());

        return new WSCampaignOutgoingMessageDTO(loggedUser.getHero().getId().toString(), loggedUser.getHero().getName(), loggedUser.getHero().getImageUrl(), incomingMessage.getMessage(), utils.formattedTimeNow());
    }
}
