package io.ethertale.findadicethymeleaf.campaign.service;

import io.ethertale.findadicethymeleaf.campaign.model.*;
import io.ethertale.findadicethymeleaf.campaign.repo.*;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.web.dto.campaign.CampaignCreateDTO;
import io.ethertale.findadicethymeleaf.web.dto.campaign.CampaignUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CampaignService {

    private final CampaignRepo campaignRepo;
    private final CampaignMembershipRepo campaignMembershipRepo;
    private final CharacterSheetRepo characterSheetRepo;
    private final DmNotesRepo dmNotesRepo;
    private final CampaignMessageRepo campaignMessageRepo;

    // Dir for where the uploaded maps will be, TODO move to application.properties
    private static final String MAP_UPLOAD_DIR = "uploads/maps";

    @Autowired
    public CampaignService(CampaignRepo campaignRepo, CampaignMembershipRepo campaignMembershipRepo, CharacterSheetRepo characterSheetRepo, DmNotesRepo dmNotesRepo, CampaignMessageRepo campaignMessageRepo) {
        this.campaignRepo = campaignRepo;
        this.campaignMembershipRepo = campaignMembershipRepo;
        this.characterSheetRepo = characterSheetRepo;
        this.dmNotesRepo = dmNotesRepo;
        this.campaignMessageRepo = campaignMessageRepo;
    }

    // CAMPAIGN

    public List<Campaign> getAllActiveCampaignsSortedByCreation() {
        return campaignRepo.findByStatus(CampaignStatus.ACTIVE).stream().sorted(Comparator.comparing(Campaign::getCreatedAt).reversed()).toList();
    }

    public Campaign getCampaignById(UUID id) {
        return campaignRepo.findById(id).orElse(null);
    }

    @Transactional
    public Campaign createCampaign(CampaignCreateDTO campaignCreateDTO, Hero dm) {
        Campaign campaign = Campaign.builder()
                .name(campaignCreateDTO.getTitle())
                .description(campaignCreateDTO.getDescription())
                .imageUrl(campaignCreateDTO.getImageUrl())
                .maxPlayers(campaignCreateDTO.getMaxPlayers())
                .status(CampaignStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .dm(dm)
                .build();

        campaignRepo.save(campaign);

        // Creating empty DmNotes for the campaign so it never returns null
        DmNotes notes = DmNotes.builder()
                .campaign(campaign)
                .content("")
                .build();
        dmNotesRepo.save(notes);

        log.info("Campaign '{}' created by hero {} at {}", campaign.getName(), dm.getName(), LocalDateTime.now());
        return campaign;
    }

    @Transactional
    public void updateCampaign(UUID campaignId, CampaignUpdateDTO dto) {
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow();
        campaign.setName(dto.getTitle());
        campaign.setDescription(dto.getDescription());
        campaign.setImageUrl(dto.getImageUrl());
        campaign.setMaxPlayers(dto.getMaxPlayers());
        campaignRepo.save(campaign);
    }

    @Transactional
    public void archiveCampaign(UUID id){
        Campaign campaign = campaignRepo.findById(id).orElseThrow();
        campaign.setStatus(CampaignStatus.ARCHIVED);
        campaignRepo.save(campaign);
        log.info("Campaign '{}' archived.", campaign.getName());
    }

    /***
    Map Uploading

     Save the file to a specific path "/uploads/maps" with the name of the file turning to
     "{CAMPAIGN_ID}_map.png" and overwriting any previous file with the same name if it exists.
     */
    @Transactional
    public String uploadMap(UUID campaignId, MultipartFile file) throws IOException {
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow();

        Path uploadDir = Paths.get(MAP_UPLOAD_DIR);
        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }

        String filename = campaignId + "_map.png";
        Path destination = uploadDir.resolve(filename);

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        // Storing the path so Thymeleaf can access it
        String mapPath = "/" + MAP_UPLOAD_DIR + "/" + filename;
        campaign.setImagePath(mapPath);
        campaignRepo.save(campaign);

        log.info("Campaign '{}' uploaded a map: {}", campaign.getName(), mapPath);

        return mapPath;
    }

    /***
     * Join requests & Membership management
     * Hero requests to join a campaign, which in turn creates a PENDING membership.
     * Campaign must be ACTIVE, not full, and hero must not already have a membership.
     */
    @Transactional
    public void requestToJoin (UUID campaignId, Hero hero){
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow();

        if (campaign.getStatus() == CampaignStatus.ARCHIVED){
            throw new IllegalStateException("Campaign '"+campaign.getName()+"' is archived.");
        }
        if (campaign.getActiveMemberCount() <= campaign.getMaxPlayers()){
            throw new IllegalStateException("Campaign '"+campaign.getName()+"' is full!");
        }
        if (campaignMembershipRepo.existsByCampaignAndHero(campaign, hero)){
            throw new IllegalStateException("Campaign '"+campaign.getName()+"' is already member!");
        }

        CampaignMembership campaignMembership = CampaignMembership.builder()
                .campaign(campaign)
                .hero(hero)
                .status(MembershipStatus.PENDING)
                .joinedAt(LocalDateTime.now())
                .build();

        campaignMembershipRepo.save(campaignMembership);

        log.info("Hero {} requested to join campaign '{}'.", hero.getName(), campaign.getName());
    }

    // DM Approved membership
    @Transactional
    public void approveMembership(UUID membershipId){
        CampaignMembership campaignMembership = campaignMembershipRepo.findById(membershipId).orElseThrow();
        campaignMembership.setStatus(MembershipStatus.ACTIVE);
        campaignMembershipRepo.save(campaignMembership);

        CharacterSheet sheet = CharacterSheet.builder()
                .membership(campaignMembership)
                .build();
        characterSheetRepo.save(sheet);

        log.info("Hero {} approved for campaign '{}'.", campaignMembership.getHero().getName(), campaignMembership.getCampaign().getName());
    }

    // DM Kicks a player, archiving their membership for future reference/invite
    @Transactional
    public void kickMember(UUID membershipId){
        CampaignMembership campaignMembership = campaignMembershipRepo.findById(membershipId).orElseThrow();
        campaignMembership.setStatus(MembershipStatus.ARCHIVED);
        campaignMembershipRepo.save(campaignMembership);

        log.info("Hero {} kicked from campaign '{}'.",
                campaignMembership.getHero().getName(), campaignMembership.getCampaign().getName());
    }


}
