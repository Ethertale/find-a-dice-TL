package io.ethertale.findadicethymeleaf.moderation.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordListProfanityFilterTest {

    private final WordListProfanityFilter filter = new WordListProfanityFilter();

    @Test
    void cleanTextIsReturnedUnchanged() {
        String text = "The rogue rolled a natural 20 against the cockatrice!";
        assertEquals(text, filter.mask(text));
    }

    @Test
    void bannedWordIsMaskedKeepingLength() {
        assertEquals("what the **** is this", filter.mask("what the fuck is this"));
    }

    @Test
    void caseLeetAndPunctuationTricksAreCaught() {
        assertEquals("****", filter.mask("SHIT"));
        assertEquals("****", filter.mask("sh1t"));
        assertEquals("***", filter.mask("a$$"));
        assertEquals("*******", filter.mask("s.h.i.t"));
        assertEquals("oh *****", filter.mask("oh shit!"));
    }

    @Test
    void repeatedLettersAreCaught() {
        assertEquals("*********", filter.mask("fuuuuuuck"));
        assertEquals("******", filter.mask("shiiit"));
    }

    @Test
    void shortBannedWordsStillMatchUncollapsed() {
        assertEquals("you ***", filter.mask("you ass"));
    }

    @Test
    void innocentWordsContainingBannedOnesAreNotMasked() {
        String text = "The assassin passed the class and drank a cocktail in Scunthorpe";
        assertEquals(text, filter.mask(text));
    }

    @Test
    void nullAndBlankAreSafe() {
        assertNull(filter.mask(null));
        assertEquals("  ", filter.mask("  "));
    }
}
