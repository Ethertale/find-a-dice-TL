package io.ethertale.findadicethymeleaf.hero.model;

public enum Classes {
    ARTIFICER, BARBARIAN, BARD, BLOOD_HUNTER, CLERIC, DRUID, FIGHTER, GUNSLINGER, MONK, PALADIN, RANGER, ROGUE, SORCERER, WARLOCK, WIZARD, OTHER;

    @Override
    public String toString() {
        return name()
                .replace("_", " ");
    }
}

