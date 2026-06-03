package io.ethertale.findadicethymeleaf.campaign.repo;

import io.ethertale.findadicethymeleaf.campaign.model.Campaign;
import io.ethertale.findadicethymeleaf.campaign.model.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignRepo extends JpaRepository<Campaign, UUID> {

    List<Campaign> findByStatus(CampaignStatus status);
    List<Campaign> findByTitleContainingIgnoreCase(String query);
}
