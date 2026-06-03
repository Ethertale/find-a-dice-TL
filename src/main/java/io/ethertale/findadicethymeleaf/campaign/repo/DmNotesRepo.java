package io.ethertale.findadicethymeleaf.campaign.repo;

import io.ethertale.findadicethymeleaf.campaign.model.Campaign;
import io.ethertale.findadicethymeleaf.campaign.model.DmNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DmNotesRepo extends JpaRepository<DmNotes, UUID> {
    Optional<DmNotes> findByCampaign(Campaign campaign);
}
