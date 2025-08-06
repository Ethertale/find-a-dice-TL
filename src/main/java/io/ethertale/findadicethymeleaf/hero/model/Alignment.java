package io.ethertale.findadicethymeleaf.hero.model;

public enum Alignment {
    LAWFUL_GOOD,
    NEUTRAL_GOOD,
    CHAOTIC_GOOD,
    LAWFUL_NEUTRAL,
    TRUE_NEUTRAL,
    CHAOTIC_NEUTRAL,
    LAWFUL_EVIL,
    NEUTRAL_EVIL,
    CHAOTIC_EVIL;

    @Override
    public String toString() {
        return name()
                .replace("_", " ");
    }
}
