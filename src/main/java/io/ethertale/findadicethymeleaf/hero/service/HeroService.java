package io.ethertale.findadicethymeleaf.hero.service;

import io.ethertale.findadicethymeleaf.hero.model.*;
import io.ethertale.findadicethymeleaf.hero.repo.HeroRepo;
import io.ethertale.findadicethymeleaf.user.model.Genders;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.repo.UserRepo;
import io.ethertale.findadicethymeleaf.web.dto.HeroUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class HeroService {

    private final UserRepo userRepo;
    private final HeroRepo heroRepo;

    @Autowired
    public HeroService(UserRepo userRepo, HeroRepo heroRepo) {
        this.userRepo = userRepo;
        this.heroRepo = heroRepo;
    }

    public Hero createFirstHero(User user) {
        Hero hero = Hero.builder()
                .user(user)
                .name("My Hero")
                .age(18)
                .gender(Genders.MALE)
                .race(Races.AARAKOCRA)
                .charClass(Classes.ARTIFICER)
                .level(1)
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
                .groupPosts(new HashSet<>())
                .comments(new HashSet<>())
                .groups(new HashSet<>())
                .createdGroups(new HashSet<>())
                .build();

        heroRepo.save(hero);
        return hero;
    }

    public void updateHero(Hero heroToUpdate, HeroUpdateDTO heroUpdateDTO) {
        Hero updatedHero = heroRepo.findById(heroToUpdate.getId()).orElse(null);

        if (heroUpdateDTO.getName() != null || !heroUpdateDTO.getName().isBlank()) {
            updatedHero.setName(heroUpdateDTO.getName());
        }
        if (heroUpdateDTO.getAge() >= 18) {
            updatedHero.setAge(heroUpdateDTO.getAge());
        }
        if (heroUpdateDTO.getRace() != null) {
            updatedHero.setRace(heroUpdateDTO.getRace());
        }
        if (heroUpdateDTO.getGender() != null) {
            updatedHero.setGender(heroUpdateDTO.getGender());
        }
        if (heroUpdateDTO.getCharClass() != null) {
            updatedHero.setCharClass(heroUpdateDTO.getCharClass());
        }
        if (heroUpdateDTO.getLevel() >= 1 && heroUpdateDTO.getLevel() <= 99) {
            updatedHero.setLevel(heroUpdateDTO.getLevel());
        }
        if (heroUpdateDTO.getDescription() != null || !heroUpdateDTO.getDescription().isBlank()) {
            updatedHero.setDescription(heroUpdateDTO.getDescription());
        }
        if (heroUpdateDTO.getImageUrl() != null || !heroUpdateDTO.getDescription().isBlank()) {
            updatedHero.setImageUrl(heroUpdateDTO.getImageUrl());
        }
        if (heroUpdateDTO.getAlignment() != null) {
            updatedHero.setAlignment(heroUpdateDTO.getAlignment());
        }
        if (heroUpdateDTO.getBackground() != null) {
            updatedHero.setBackground(heroUpdateDTO.getBackground());
        }
        if (heroUpdateDTO.getStrength() >= 1 && heroUpdateDTO.getStrength() <= 20) {
            updatedHero.setStrength(heroUpdateDTO.getStrength());
        }
        if (heroUpdateDTO.getDexterity() >= 1 && heroUpdateDTO.getDexterity() <= 20) {
            updatedHero.setDexterity(heroUpdateDTO.getDexterity());
        }
        if (heroUpdateDTO.getConstitution() >= 1 && heroUpdateDTO.getConstitution() <= 20) {
            updatedHero.setConstitution(heroUpdateDTO.getConstitution());
        }
        if (heroUpdateDTO.getIntelligence() >= 1 && heroUpdateDTO.getIntelligence() <= 20) {
            updatedHero.setIntelligence(heroUpdateDTO.getIntelligence());
        }
        if (heroUpdateDTO.getWisdom() >= 1 && heroUpdateDTO.getWisdom() <= 20) {
            updatedHero.setWisdom(heroUpdateDTO.getWisdom());
        }
        if (heroUpdateDTO.getCharisma() >= 1 && heroUpdateDTO.getCharisma() <= 20) {
            updatedHero.setCharisma(heroUpdateDTO.getCharisma());
        }

        heroRepo.save(updatedHero);

    }

    public List<Classes> getAllCharClasses() {
        return List.of(Classes.values());
    }

    public List<Alignment> getAllAlignments() {
        return List.of(Alignment.values());
    }

    public List<Backgrounds> getAllBackgrounds() {
        return List.of(Backgrounds.values());
    }

    public List<Genders> getAllGenders() {
        return List.of(Genders.values());
    }
    public List<Races> getAllRaces() {
        return List.of(Races.values());
    }
}
