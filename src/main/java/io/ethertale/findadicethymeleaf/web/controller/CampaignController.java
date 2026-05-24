package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.campaign.model.Campaign;
import io.ethertale.findadicethymeleaf.campaign.model.CampaignMembership;
import io.ethertale.findadicethymeleaf.campaign.model.MembershipStatus;
import io.ethertale.findadicethymeleaf.campaign.service.CampaignService;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.service.UserService;
import io.ethertale.findadicethymeleaf.web.dto.campaign.CampaignCreateDTO;
import io.ethertale.findadicethymeleaf.web.dto.campaign.CampaignUpdateDTO;
import io.ethertale.findadicethymeleaf.web.dto.campaign.CharacterSheetDTO;
import io.ethertale.findadicethymeleaf.web.dto.campaign.DmNotesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
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

    // Campaign view - it will differ based on role (isDm/isMember/isPending)
    @GetMapping("/{id}")
    public ModelAndView campaignView(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());
        Campaign campaign = campaignService.getCampaignById(id);

        if (campaign == null) {
            return new ModelAndView("redirect:/campaigns");
        }

        boolean isDm = campaignService.isDm(id, loggedUser.getHero());
        boolean isActiveMember = campaignService.isActiveMember(id, loggedUser.getHero());
        CampaignMembership membership = campaignService.getMembership(id, loggedUser.getHero());

        ModelAndView mav = new ModelAndView("campaignView");
        mav.addObject("campaign", campaign);
        mav.addObject("loggedUser", loggedUser);
        mav.addObject("isDm", isDm);
        mav.addObject("isActiveMember", isActiveMember);
        mav.addObject("activeMembers", campaignService.getActiveMembers(id));
        mav.addObject("messages", campaignService.getMessages(id));

        // Pass membership-specific objects if the user has a role in this campaign
        if (isDm){
            mav.addObject("pendingRequests", campaignService.getAllPendingRequests(id));
            mav.addObject("dmNotes", campaignService.getDmNotes(id));
        }
        if (isActiveMember && membership != null){
            mav.addObject("membership", membership);
            mav.addObject("characterSheet", campaignService.getSheetForMembership(membership.getId()));
            mav.addObject("sheetDTO", new CharacterSheetDTO());
        }

        // Pending Player - they can see the campaign exist but can't interact with it
        if (!isDm && !isActiveMember){
            boolean isPending = membership != null && membership.getStatus() == MembershipStatus.PENDING;
            mav.addObject("isPending", isPending);
        }

        return mav;
    }

    // Update Campaign (DM Only)
    @PostMapping("/{id}/update")
    public String updateCampaign(@PathVariable UUID id, @ModelAttribute CampaignUpdateDTO updateDTO, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        if (!campaignService.isDm(id, loggedUser.getHero())) {
            return "redirect:/campaigns/" + id;
        }

        campaignService.updateCampaign(id, updateDTO);
        return "redirect:/campaigns/" + id;
    }

    // Archive Campaign (DM Only)
    @PostMapping("/{id}/archive")
    public String archiveCampaign(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        if (!campaignService.isDm(id, loggedUser.getHero())) {
            return "redirect:/campaigns/" + id;
        }

        campaignService.archiveCampaign(id);
        return "redirect:/campaigns";
    }

    // Map Upload (DM Only)
    @PostMapping("/{id}/upload-map")
    public String uploadMap(@PathVariable UUID id, @RequestParam("mapFile")MultipartFile file, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        if (!campaignService.isDm(id, loggedUser.getHero())) {
            return "redirect:/campaigns/" + id;
        }

        try {
            campaignService.uploadMap(id, file);
        } catch (IOException e) {
            // Redirect back, TODO add error message
            return "redirect:/campaigns/" + id + "?mapError=true";
        }

        return "redirect:/campaigns/" + id;
    }

    //Join request
    @PostMapping("/{id}/request-join")
    public String requestJoin(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        // DM cannot request to join their own campaign
        if (campaignService.isDm(id, loggedUser.getHero())) {
            return "redirect:/campaigns/" + id;
        }

        try {
            campaignService.requestToJoin(id, loggedUser.getHero());
        } catch (IllegalStateException e) {
            return "redirect:/campaigns/" + id + "?joinError=true";
        }

        return "redirect:/campaigns/" + id;
    }

    // Approve / Reject / Kick (DM Only)
    @PostMapping("/{id}/approve/{membershipId}")
    public String approveMember(@PathVariable UUID id, @PathVariable UUID membershipId, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        if (!campaignService.isDm(id, loggedUser.getHero())) {
            return "redirect:/campaigns/" + id;
        }

        campaignService.approveMembership(membershipId);
        return "redirect:/campaigns/" + id;
    }

    @PostMapping("/{id}/reject/{membershipId}")
    public String rejectMember(@PathVariable UUID id, @PathVariable UUID membershipId, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        if (!campaignService.isDm(id, loggedUser.getHero())) {
            return "redirect:/campaigns/" + id;
        }

        campaignService.rejectMembership(membershipId);
        return "redirect:/campaigns/" + id;
    }

    @PostMapping("/{id}/kick/{membershipId}")
    public String kickMember(@PathVariable UUID id, @PathVariable UUID membershipId, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        if (!campaignService.isDm(id, loggedUser.getHero())) {
            return "redirect:/campaigns/" + id;
        }

        campaignService.kickMember(membershipId);
        return "redirect:/campaigns/" + id;
    }

    // Character Sheet (For the players)
    @PostMapping("/{id}/sheet/{membershipId}/update")
    public String updateSheet(@PathVariable UUID id, @PathVariable UUID membershipId, @ModelAttribute CharacterSheetDTO sheetDTO, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        if (!campaignService.isActiveMember(id, loggedUser.getHero())) {
            return "redirect:/campaigns/" + id;
        }

        campaignService.updateSheet(membershipId, sheetDTO);
        return "redirect:/campaigns/" + id;
    }

    // DM Notes (For the DM)
    @PostMapping("/{id}/notes/update")
    public String updateDmNotes(@PathVariable UUID id, @ModelAttribute DmNotesDTO notesDTO, @AuthenticationPrincipal AuthenticationDetails authenticationDetails){
        User loggedUser = userService.getUserById(authenticationDetails.getId());

        if (!campaignService.isDm(id, loggedUser.getHero())) {
            return "redirect:/campaigns/" + id;
        }

        campaignService.updateDmNotes(id, notesDTO);
        return "redirect:/campaigns/" + id;
    }
}
