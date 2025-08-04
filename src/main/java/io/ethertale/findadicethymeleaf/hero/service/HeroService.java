package io.ethertale.findadicethymeleaf.hero.service;

import io.ethertale.findadicethymeleaf.hero.model.Alignment;
import io.ethertale.findadicethymeleaf.hero.model.Backgrounds;
import io.ethertale.findadicethymeleaf.hero.model.Classes;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.hero.repo.HeroRepo;
import io.ethertale.findadicethymeleaf.user.model.Genders;
import io.ethertale.findadicethymeleaf.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HeroService {

    private final HeroRepo heroRepo;

    @Autowired
    public HeroService(HeroRepo heroRepo) {
        this.heroRepo = heroRepo;
    }

    public Hero createFirstHero(User user) {
        Hero hero = Hero.builder()
                .user(user)
                .name("My Hero")
                .age(18)
                .gender(Genders.MALE)
                .charClass(Classes.ARTIFICER)
                .description("This is my description")
                .imageUrl("https://i.ibb.co/WWDv4mYx/Logo-Transparent.png")
                .alignment(Alignment.TRUE_NEUTRAL)
                .background(Backgrounds.ATHLETE)
                .strength(1)
                .dexterity(1)
                .constitution(1)
                .intelligence(1)
                .wisdom(1)
                .charisma(1)
                .createdAt(LocalDateTime.now())
                .build();

        heroRepo.save(hero);
        return hero;
    }
}
