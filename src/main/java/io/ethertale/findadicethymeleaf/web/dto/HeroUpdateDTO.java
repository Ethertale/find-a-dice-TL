package io.ethertale.findadicethymeleaf.web.dto;

import io.ethertale.findadicethymeleaf.hero.model.*;
import io.ethertale.findadicethymeleaf.user.model.Genders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeroUpdateDTO {
    private String name;
    private int age;
    private Races race;
    private Genders gender;
    private Classes charClass;
    private int level;
    private String description;
    private String imageUrl;
    private Alignment alignment;
    private Backgrounds background;
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;

    public static HeroUpdateDTO populateInfoHero(Hero hero) {
        //Set a default DTO populated with the heroes stats
        HeroUpdateDTO dto = new HeroUpdateDTO();

        dto.setName(hero.getName());
        dto.setAge(hero.getAge());
        dto.setRace(hero.getRace());
        dto.setGender(hero.getGender());
        dto.setCharClass(hero.getCharClass());
        dto.setLevel(hero.getLevel());
        dto.setDescription(hero.getDescription());
        dto.setImageUrl(hero.getImageUrl());
        dto.setAlignment(hero.getAlignment());
        dto.setBackground(hero.getBackground());
        dto.setStrength(hero.getStrength());
        dto.setDexterity(hero.getDexterity());
        dto.setConstitution(hero.getConstitution());
        dto.setIntelligence(hero.getIntelligence());
        dto.setWisdom(hero.getWisdom());
        dto.setCharisma(hero.getCharisma());
        return dto;
    }
}
