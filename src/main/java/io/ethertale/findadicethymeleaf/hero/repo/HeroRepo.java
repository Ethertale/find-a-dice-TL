package io.ethertale.findadicethymeleaf.hero.repo;

import io.ethertale.findadicethymeleaf.hero.model.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HeroRepo extends JpaRepository<Hero, UUID> {
}
