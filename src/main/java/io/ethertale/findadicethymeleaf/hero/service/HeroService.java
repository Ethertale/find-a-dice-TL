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

    }
}
