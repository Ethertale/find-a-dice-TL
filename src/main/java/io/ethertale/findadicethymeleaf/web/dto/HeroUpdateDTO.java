package io.ethertale.findadicethymeleaf.web.dto;

import io.ethertale.findadicethymeleaf.hero.model.Alignment;
import io.ethertale.findadicethymeleaf.hero.model.Backgrounds;
import io.ethertale.findadicethymeleaf.hero.model.Classes;
import io.ethertale.findadicethymeleaf.hero.model.Races;
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
}
