package io.ethertale.findadicethymeleaf;

import io.ethertale.findadicethymeleaf.hero.model.Alignment;
import io.ethertale.findadicethymeleaf.hero.model.Backgrounds;
import io.ethertale.findadicethymeleaf.hero.model.Classes;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.hero.repo.HeroRepo;
import io.ethertale.findadicethymeleaf.user.model.Genders;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.model.UserRoles;
import io.ethertale.findadicethymeleaf.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
public class FindADiceThymeleafApplication implements CommandLineRunner {

    private final HeroRepo heroRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FindADiceThymeleafApplication(HeroRepo heroRepo, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.heroRepo = heroRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(FindADiceThymeleafApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRepo.count() == 0){
            Hero hero1 = new Hero();
//            hero1.setId(UUID.randomUUID());
            hero1.setName("ADMIN");
            hero1.setAge(1);
            hero1.setGender(Genders.MALE);
            hero1.setCharClass(Classes.ARTIFICER);
            hero1.setAlignment(Alignment.CHAOTIC_EVIL);
            hero1.setBackground(Backgrounds.ATHLETE);
            hero1.setStrength(1);
            hero1.setDexterity(1);
            hero1.setConstitution(1);
            hero1.setConstitution(1);
            hero1.setIntelligence(1);
            hero1.setWisdom(1);
            hero1.setCharisma(1);
            hero1.setCreatedAt(LocalDateTime.now());

            User user1 = new User();
//            user1.setId(UUID.randomUUID());
            user1.setUsername("admin");
            user1.setPassword(passwordEncoder.encode("admin1"));
            user1.setEmail("email@mail.com");
            user1.setFirstName("John");
            user1.setLastName("Doe");
            user1.setDescription("Description");
            user1.setRole(UserRoles.USER);
            user1.setImageUrl("https://cdn.cloudflare.steamstatic.com/steamcommunity/public/images/avatars/77/777eb0797f3eb82da4255acb97932600ebbcb879_full.jpg");
            user1.setCreatedAt(LocalDateTime.now());

            user1.setHero(hero1);
            hero1.setUser(user1);

            userRepo.save(user1);
        }

    }
}
