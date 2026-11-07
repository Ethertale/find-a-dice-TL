package io.ethertale.findadicethymeleaf.moderation.repo;

import io.ethertale.findadicethymeleaf.moderation.model.ModerationFlag;
import io.ethertale.findadicethymeleaf.moderation.model.ModerationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModerationFlagRepo extends JpaRepository<ModerationFlag, UUID> {
    List<ModerationFlag> findAllByStatusOrderByFlaggedAtDesc(ModerationStatus status);
}
