package io.ethertale.findadicethymeleaf.campaign.repo;

import io.ethertale.findadicethymeleaf.campaign.model.CampaignMembership;
import io.ethertale.findadicethymeleaf.campaign.model.CharacterSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CharacterSheetRepo extends JpaRepository<CharacterSheet, UUID> {
    Optional<CharacterSheet> findByMembership(CampaignMembership membership);
}
