package io.ethertale.findadicethymeleaf.campaign.repo;

import io.ethertale.findadicethymeleaf.campaign.model.Campaign;
import io.ethertale.findadicethymeleaf.campaign.model.CampaignMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignMessageRepo extends JpaRepository<CampaignMessage, UUID> {
    List<CampaignMessage> findByCampaignOrderBySentAtAsc(Campaign campaign);
}
