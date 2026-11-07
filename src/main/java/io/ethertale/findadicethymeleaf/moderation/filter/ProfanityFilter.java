package io.ethertale.findadicethymeleaf.moderation.filter;

/**
 * A strategy for finding profanity in user text.
 * The current implementation is a local word list; an AI-backed filter can implement
 * this same interface later without touching any of the services that call it.
 */
public interface ProfanityFilter {

    /**
     * @return the text with every offending word replaced by asterisks,
     * or the exact same text if nothing was found.
     */
    String mask(String text);
}
