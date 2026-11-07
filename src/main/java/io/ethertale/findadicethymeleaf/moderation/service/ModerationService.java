package io.ethertale.findadicethymeleaf.moderation.service;

import io.ethertale.findadicethymeleaf.moderation.filter.ProfanityFilter;
import io.ethertale.findadicethymeleaf.moderation.model.ModerationFlag;
import io.ethertale.findadicethymeleaf.moderation.model.ModerationSource;
import io.ethertale.findadicethymeleaf.moderation.model.ModerationStatus;
import io.ethertale.findadicethymeleaf.moderation.repo.ModerationFlagRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ModerationService {

    private final ProfanityFilter profanityFilter;
    private final ModerationFlagRepo moderationFlagRepo;

    @Autowired
    public ModerationService(ProfanityFilter profanityFilter, ModerationFlagRepo moderationFlagRepo) {
        this.profanityFilter = profanityFilter;
        this.moderationFlagRepo = moderationFlagRepo;
    }

    /**
     * Runs user text through the profanity filter.
     * If anything is found, the original is stored as a PENDING flag for admin review
     * and the masked version is returned for the caller to save/display instead.
     *
     * @return the text to persist - masked if profane, untouched otherwise
     */
    @Transactional
    public String moderate(String text, ModerationSource source, UUID authorUserId) {
        String masked = profanityFilter.mask(text);
        if (masked == null || masked.equals(text)) {
            return text;
        }

        ModerationFlag flag = ModerationFlag.builder()
                .source(source)
                .status(ModerationStatus.PENDING)
                .authorUserId(authorUserId)
                .originalText(text)
                .maskedText(masked)
                .flaggedAt(LocalDateTime.now())
                .build();
        moderationFlagRepo.save(flag);

        log.warn("Profanity flagged in {} by user {}.", source, authorUserId);
        return masked;
    }

    public boolean isProfane(String text) {
        String masked = profanityFilter.mask(text);
        return masked != null && !masked.equals(text);
    }

    public List<ModerationFlag> getAllPendingFlags() {
        return moderationFlagRepo.findAllByStatusOrderByFlaggedAtDesc(ModerationStatus.PENDING);
    }

    @Transactional
    public void markReviewed(UUID flagId, UUID reviewerUserId) {
        ModerationFlag flag = moderationFlagRepo.findById(flagId).orElseThrow();
        flag.setStatus(ModerationStatus.REVIEWED);
        moderationFlagRepo.save(flag);
        log.info("Moderation flag {} marked as reviewed by user {}.", flagId, reviewerUserId);
    }
}
