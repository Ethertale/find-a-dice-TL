package io.ethertale.findadicethymeleaf.moderation.model;

public enum ModerationSource {
    USER_REGISTRATION,
    HERO,
    GROUP,
    GROUP_POST,
    EVENT,
    CHAT_MESSAGE,
    CAMPAIGN,
    CAMPAIGN_CHAT_MESSAGE,
    CHARACTER_SHEET;

    @Override
    public String toString() {
        return name()
                .replace("_", " ");
    }
}
