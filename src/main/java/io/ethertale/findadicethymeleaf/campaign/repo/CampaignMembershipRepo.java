package io.ethertale.findadicethymeleaf.campaign.repo;

import io.ethertale.findadicethymeleaf.campaign.model.Campaign;
import io.ethertale.findadicethymeleaf.campaign.model.CampaignMembership;
import io.ethertale.findadicethymeleaf.campaign.model.MembershipStatus;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampaignMembershipRepo extends JpaRepository<CampaignMembership, UUID> {
    Optional<CampaignMembership> findByCampaignAndHero(Campaign campaign, Hero hero);
    List<CampaignMembership> findByCampaignAndStatus(Campaign campaign, MembershipStatus status);
    List<CampaignMembership> findByHeroAndStatus(Hero hero, MembershipStatus status);
    boolean existsByCampaignAndHero(Campaign campaign, Hero hero);
}
