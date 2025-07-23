package io.ethertale.findadicethymeleaf.hero.service;

import io.ethertale.findadicethymeleaf.hero.model.Alignment;
import io.ethertale.findadicethymeleaf.hero.model.Backgrounds;
import io.ethertale.findadicethymeleaf.hero.model.Classes;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.hero.repo.HeroRepo;
import io.ethertale.findadicethymeleaf.user.model.Genders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class HeroService {

    private final HeroRepo heroRepo;

    @Autowired
    public HeroService(HeroRepo heroRepo) {
        this.heroRepo = heroRepo;
    }

    public void createHero() {
//        Hero hero = Hero.builder()
//                .id(UUID.randomUUID())
//                .name("Hero")
//                .age(1)
//                .gender(Genders.MALE)
//                .charClass(Classes.ARTIFICER)
//                .alignment(Alignment.CHAOTIC_EVIL)
//                .background(Backgrounds.ATHLETE)
//                .strength(1)
//                .dexterity(1)
//                .constitution(1)
//                .intelligence(1)
//                .wisdom(1)
//                .charisma(1)
//                .createdAt(LocalDateTime.now())
//                .build();
        Hero hero = new Hero();
        hero.setId(UUID.randomUUID());
        hero.setName("hero");
        hero.setAge(1);
        hero.setGender(Genders.MALE);
        hero.setCharClass(Classes.ARTIFICER);
        hero.setAlignment(Alignment.CHAOTIC_EVIL);
        hero.setBackground(Backgrounds.ATHLETE);
        hero.setStrength(1);
        hero.setDexterity(1);
        hero.setConstitution(1);
        hero.setConstitution(1);
        hero.setIntelligence(1);
        hero.setWisdom(1);
        hero.setCharisma(1);
        hero.setCreatedAt(LocalDateTime.now());
    }
}
