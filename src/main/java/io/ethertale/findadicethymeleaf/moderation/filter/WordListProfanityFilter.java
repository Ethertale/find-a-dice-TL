package io.ethertale.findadicethymeleaf.moderation.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Local, list-based profanity filter.
 * Word lists live in resources/moderation/ - one word per line, lines starting with # are comments.
 *  - banned-words.txt  -> words that get masked
 *  - allowed-words.txt -> words that must never be masked even if they look like a banned one (e.g. "assassin", "cockatrice")
 */
@Slf4j
@Component
public class WordListProfanityFilter implements ProfanityFilter {

    // A "word" is any run of letters, digits and the symbols people use to dodge filters (sh!t, a$$, f*ck, s.h.i.t)
    private static final Pattern TOKEN = Pattern.compile("[\\p{L}\\p{N}@$!*+._-]+");
    private static final Pattern TRAILING_PUNCTUATION = Pattern.compile("[!.?_-]+$");
    private static final Pattern NON_LETTERS = Pattern.compile("[^\\p{L}]");

    private static final Map<Character, Character> LEET = Map.of(
            '0', 'o', '1', 'i', '3', 'e', '4', 'a', '5', 's',
            '7', 't', '@', 'a', '$', 's', '!', 'i', '+', 't'
    );

    private final Set<String> bannedWords;
    private final Set<String> allowedWords;

    public WordListProfanityFilter() {
        this.bannedWords = loadWordList("moderation/banned-words.txt");
        this.allowedWords = loadWordList("moderation/allowed-words.txt");
        log.info("Profanity filter loaded {} banned and {} allowed words.", bannedWords.size(), allowedWords.size());
    }

    @Override
    public String mask(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }

        Matcher matcher = TOKEN.matcher(text);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String token = matcher.group();
            String replacement = isProfane(normalize(token)) ? "*".repeat(token.length()) : token;
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * Decides whether a single normalized word is profane.
     *
     * @param word already lowercased, leet-decoded and stripped to letters only
     *             ("Sh1t!" -> "shit", "a$$" -> "ass", "s.h.i.t" -> "shit")
     */
    boolean isProfane(String word) {
        if (word.isEmpty() || allowedWords.contains(word)) {
            return false;
        }
        // Check the word as typed AND with repeats collapsed - collapsing alone would turn "ass" into "as" and miss it
        return bannedWords.contains(word) || bannedWords.contains(collapseRepeats(word));
    }

    // "fuuuuck" -> "fuck". Collapses every run of the same letter down to one.
    String collapseRepeats(String word) {
        return word.replaceAll("(\\p{L})\\1+", "$1");
    }

    String normalize(String token) {
        String core = TRAILING_PUNCTUATION.matcher(token).replaceAll("").toLowerCase();

        StringBuilder decoded = new StringBuilder(core.length());
        for (char c : core.toCharArray()) {
            decoded.append(LEET.getOrDefault(c, c));
        }

        return NON_LETTERS.matcher(decoded).replaceAll("");
    }

    private Set<String> loadWordList(String path) {
        Set<String> words = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    words.add(line);
                }
            }
        } catch (IOException e) {
            log.error("Could not load word list {}", path, e);
        }
        return words;
    }
}
