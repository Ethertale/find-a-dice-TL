package io.ethertale.findadicethymeleaf.campaign.service;

import io.ethertale.findadicethymeleaf.campaign.model.*;
import io.ethertale.findadicethymeleaf.campaign.repo.*;
import io.ethertale.findadicethymeleaf.exceptions.*;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.web.dto.campaign.CampaignCreateDTO;
import io.ethertale.findadicethymeleaf.web.dto.campaign.CampaignUpdateDTO;
import io.ethertale.findadicethymeleaf.web.dto.campaign.CharacterSheetDTO;
import io.ethertale.findadicethymeleaf.web.dto.campaign.DmNotesDTO;
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
import java.util.Optional;
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
//        Campaign campaign = Campaign.builder()
//                // 50 Char max
//                .title(campaignCreateDTO.getTitle())
//                // 1000 Char Max
//                .description(campaignCreateDTO.getDescription())
//                // If link does not start with HTTPS://
//                .imageUrl(campaignCreateDTO.getImageUrl())
//                // 1-15
//                .maxPlayers(campaignCreateDTO.getMaxPlayers())
//                .status(CampaignStatus.ACTIVE)
//                .createdAt(LocalDateTime.now())
//                .dm(dm)
//                .build();

        Campaign campaign = new Campaign();

        if (campaignCreateDTO.getTitle().length() > 50 || campaignCreateDTO.getTitle().length() <= 0 || campaignCreateDTO.getTitle().isBlank()) {
            throw new CampaignCreateTitleNotWithinBounds();
        }
        campaign.setTitle(campaignCreateDTO.getTitle());

        if (campaignCreateDTO.getDescription().length() > 1000 || campaignCreateDTO.getDescription().length() <= 0 || campaignCreateDTO.getDescription().isBlank()) {
            throw new CampaignCreateDescriptionNotWithinBounds();
        }
        campaign.setDescription(campaignCreateDTO.getDescription());

        if (!campaignCreateDTO.getImageUrl().startsWith("https://") || campaignCreateDTO.getImageUrl().isBlank()) {
            campaign.setImageUrl(dm.getImageUrl());
        }
        campaign.setImageUrl(campaignCreateDTO.getImageUrl());
        campaign.setImagePath("/static/imgs/new-background.jpeg");

        if (campaignCreateDTO.getMaxPlayers() > 15 || campaignCreateDTO.getMaxPlayers() <= 0) {
            throw new CampaignCreateInvalidMaxPlayers();
        }
        campaign.setMaxPlayers(campaignCreateDTO.getMaxPlayers());

        campaign.setStatus(CampaignStatus.ACTIVE);
        campaign.setCreatedAt(LocalDateTime.now());
        campaign.setDm(dm);

        campaignRepo.save(campaign);

        // Creating empty DmNotes for the campaign so it never returns null
        DmNotes notes = DmNotes.builder()
                .campaign(campaign)
                .content("")
                .build();
        dmNotesRepo.save(notes);

        log.info("Campaign '{}' created by hero {} at {}", campaign.getTitle(), dm.getName(), LocalDateTime.now());
        return campaign;
    }

    @Transactional
    public void updateCampaign(UUID campaignId, CampaignUpdateDTO dto) {
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow();
        campaign.setTitle(dto.getTitle());
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
        log.info("Campaign '{}' archived.", campaign.getTitle());
    }

    /***
    Map Uploading

     Save the file to a specific path "/uploads/maps" with the name of the file turning to
     "{CAMPAIGN_ID}_map.png" and overwriting any previous file with the same name if it exists.
     */
    @Transactional
    public String uploadMap(UUID campaignId, MultipartFile file) throws IOException {
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow(CampaignDoesNotExist::new);

        Path uploadDir = Paths.get(MAP_UPLOAD_DIR);
        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }

        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        if (!fileExtension.equals(".png") && !fileExtension.equals(".jpg") && !fileExtension.equals(".jpeg")) {
            throw new CampaignCreateInvalidImageUrl();
        }
        String filename = campaignId + "_map" + fileExtension;
        Path destination = uploadDir.resolve(filename);

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        // Storing the path so Thymeleaf can access it
        String mapPath = "/" + MAP_UPLOAD_DIR + "/" + filename;
        campaign.setImagePath(mapPath);
        campaignRepo.save(campaign);

        log.info("Campaign '{}' uploaded a map: {}", campaign.getTitle(), mapPath);

        return mapPath;
    }

    /***
     * Join requests & Membership management
     * Hero requests to join a campaign, which in turn creates a PENDING membership.
     * Campaign must be ACTIVE, not full, and hero must not already have a membership.
     */
    @Transactional
    public void requestToJoin (UUID campaignId, Hero hero){
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow(CampaignDoesNotExist::new);

        if (campaign.getStatus() == CampaignStatus.ARCHIVED){
            throw new IllegalStateException("Campaign '"+campaign.getTitle()+"' is archived.");
        }
        if (campaign.getActiveMemberCount() >= campaign.getMaxPlayers()){
            throw new IllegalStateException("Campaign '"+campaign.getTitle()+"' is full!");
        }

        // If a player has a membership but has been kicked, they can reapply again
        boolean hasActiveMembership = campaignMembershipRepo
                .findByCampaignAndHero(campaign, hero)
                .map(membership -> membership.getStatus() == MembershipStatus.ACTIVE
                        || membership.getStatus() == MembershipStatus.PENDING)
                .orElse(false);
        if (hasActiveMembership) {
            throw new IllegalStateException("Already a member or request pending.");
        }

        Optional<CampaignMembership> existing = campaignMembershipRepo.findByCampaignAndHero(campaign, hero);
        if (existing.isPresent()) {
            // Reuse the archived membership by flipping it back to PENDING
            CampaignMembership membership = existing.get();
            membership.setStatus(MembershipStatus.PENDING);
            membership.setJoinedAt(LocalDateTime.now());
            campaignMembershipRepo.save(membership);
        } else {
            // Fresh request - create a new membership
            CampaignMembership membership = CampaignMembership.builder()
                    .campaign(campaign)
                    .hero(hero)
                    .status(MembershipStatus.PENDING)
                    .joinedAt(LocalDateTime.now())
                    .build();
            campaignMembershipRepo.save(membership);
        }

        log.info("Hero {} requested to join campaign '{}'.", hero.getName(), campaign.getTitle());
    }

    // DM Approved membership
    @Transactional
    public void approveMembership(UUID membershipId){
        CampaignMembership campaignMembership = campaignMembershipRepo.findById(membershipId).orElseThrow(CampaignMembershipDoesNotExist::new);
        campaignMembership.setStatus(MembershipStatus.ACTIVE);
        campaignMembershipRepo.save(campaignMembership);

        CharacterSheet sheet = CharacterSheet.builder()
                .membership(campaignMembership)
                .build();
        characterSheetRepo.save(sheet);

        log.info("Hero {} approved for campaign '{}'.", campaignMembership.getHero().getName(), campaignMembership.getCampaign().getTitle());
    }

    // DM Kicks a player, archiving their membership for future reference/invite
    @Transactional
    public void kickMember(UUID membershipId){
        CampaignMembership campaignMembership = campaignMembershipRepo.findById(membershipId).orElseThrow(CampaignMembershipDoesNotExist::new);
        campaignMembership.setStatus(MembershipStatus.ARCHIVED);
        campaignMembershipRepo.save(campaignMembership);

        log.info("Hero {} kicked from campaign '{}'.",
                campaignMembership.getHero().getName(), campaignMembership.getCampaign().getTitle());
    }

    // DM Rejects a request, thus archiving it immediately
    @Transactional
    public void rejectMembership(UUID membershipId){
        CampaignMembership campaignMembership = campaignMembershipRepo.findById(membershipId).orElseThrow(CampaignMembershipDoesNotExist::new);
        campaignMembership.setStatus(MembershipStatus.ARCHIVED);
        campaignMembershipRepo.save(campaignMembership);
    }

    public List<CampaignMembership> getAllPendingRequests(UUID campaignId){
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow(CampaignDoesNotExist::new);
        return campaignMembershipRepo.findByCampaignAndStatus(campaign, MembershipStatus.PENDING);
    }

    public List<CampaignMembership> getActiveMembers(UUID campaignId) {
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow(CampaignDoesNotExist::new);
        return campaignMembershipRepo.findByCampaignAndStatus(campaign, MembershipStatus.ACTIVE);
    }

    // Character Sheets
    public CharacterSheet getSheetForMembership(UUID membershipId){
        CampaignMembership campaignMembership = campaignMembershipRepo.findById(membershipId).orElseThrow(CampaignMembershipDoesNotExist::new);
        return characterSheetRepo.findByMembership(campaignMembership).orElseThrow(CampaignMembershipDoesNotExist::new);
    }

    @Transactional
    public void updateSheet(UUID membershipId, CharacterSheetDTO sheetDTO){
        CampaignMembership campaignMembership = campaignMembershipRepo.findById(membershipId).orElseThrow(CampaignMembershipDoesNotExist::new);
        CharacterSheet sheet = characterSheetRepo.findByMembership(campaignMembership).orElseThrow(CampaignSheetDoesNotExist::new);

        sheet.setName(sheetDTO.getName());
        sheet.setAge(sheetDTO.getAge());
        sheet.setGender(sheetDTO.getGender());
        sheet.setRace(sheetDTO.getRace());
        sheet.setCharClass(sheetDTO.getCharClass());
        sheet.setLevel(sheetDTO.getLevel());
        sheet.setBackground(sheetDTO.getBackground());
        sheet.setAlignment(sheetDTO.getAlignment());
        sheet.setImageUrl(sheetDTO.getImageUrl());
        sheet.setHealthPoints(sheetDTO.getHp());
        sheet.setMaxHealthPoints(sheetDTO.getMaxHp());
        sheet.setResourceName(sheetDTO.getResourceName());
        sheet.setCurrResource(sheetDTO.getCurrentResource());
        sheet.setMaxResource(sheetDTO.getMaxResource());
        sheet.setStrength(sheetDTO.getStrength());
        sheet.setDexterity(sheetDTO.getDexterity());
        sheet.setConstitution(sheetDTO.getConstitution());
        sheet.setIntelligence(sheetDTO.getIntelligence());
        sheet.setWisdom(sheetDTO.getWisdom());
        sheet.setCharisma(sheetDTO.getCharisma());
        sheet.setBackstory(sheetDTO.getBackstory());
        sheet.setInventory(sheetDTO.getInventory());
        sheet.setSpells(sheetDTO.getSpells());
        sheet.setNotes(sheetDTO.getNotes());

        characterSheetRepo.save(sheet);
    }

    //DM Notes

    public DmNotes getDmNotes(UUID campaignId){
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow(CampaignDoesNotExist::new);
        return dmNotesRepo.findByCampaign(campaign).orElseThrow();
    }

    @Transactional
    public void updateDmNotes(UUID campaignId, DmNotesDTO dmNotesDTO){
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow(CampaignDoesNotExist::new);
        DmNotes dmNotes = dmNotesRepo.findByCampaign(campaign).orElseThrow(CampaignDmNotesDoNotExist::new);

        dmNotes.setContent(dmNotesDTO.getContent());
        dmNotesRepo.save(dmNotes);
    }

    // Messages

    public List<CampaignMessage> getMessages(UUID campaignId){
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow(CampaignDoesNotExist::new);
        return campaignMessageRepo.findByCampaignOrderBySentAtAsc(campaign);
    }

    @Transactional
    public CampaignMessage saveMessage(UUID campaignId, Hero sender, String message){
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow(CampaignDoesNotExist::new);

        CampaignMessage campaignMessage = CampaignMessage.builder()
                .campaign(campaign)
                .sender(sender)
                .message(message)
                .sentAt(LocalDateTime.now())
                .build();
        return campaignMessageRepo.save(campaignMessage);
    }

    // Authorization

    public boolean isDm(UUID campaignId, Hero hero){
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow(CampaignDoesNotExist::new);
        return campaign.getDm().getId().equals(hero.getId());
    }

    public boolean isActiveMember(UUID campaignId, Hero hero){
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow(CampaignDoesNotExist::new);
        return  campaignMembershipRepo.findByCampaignAndHero(campaign, hero).map(member -> member.getStatus() == MembershipStatus.ACTIVE).orElse(false);
    }

    public boolean isDmOrActiveMember(UUID campaignId, Hero hero){
        return isDm(campaignId, hero) || isActiveMember(campaignId, hero);
    }

    public CampaignMembership getMembership(UUID campaignId, Hero hero){
        Campaign campaign = campaignRepo.findById(campaignId).orElseThrow(CampaignDoesNotExist::new);
        return campaignMembershipRepo.findByCampaignAndHero(campaign, hero).orElse(null);
    }
}
