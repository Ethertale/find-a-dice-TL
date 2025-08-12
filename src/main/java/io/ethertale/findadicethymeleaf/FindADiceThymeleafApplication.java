package io.ethertale.findadicethymeleaf;

import io.ethertale.findadicethymeleaf.hero.model.*;
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
import java.util.HashSet;
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
            hero1.setName("Saarafenar Dol'Thalal");
            hero1.setAge(362);
            hero1.setDescription("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.");
            hero1.setGender(Genders.MALE);
            hero1.setCharClass(Classes.BLOOD_HUNTER);
            hero1.setAlignment(Alignment.CHAOTIC_NEUTRAL);
            hero1.setBackground(Backgrounds.KNIGHT_OF_THE_ORDER);
            hero1.setRace(Races.ELF_DROW);
            hero1.setLevel(12);
            hero1.setStrength(15);
            hero1.setDexterity(8);
            hero1.setConstitution(14);
            hero1.setIntelligence(7);
            hero1.setWisdom(11);
            hero1.setCharisma(9);
            hero1.setImageUrl("https://files.idyllic.app/files/static/3349488?width=256&optimizer=image");
            hero1.setCreatedAt(LocalDateTime.now());
            hero1.setGroupPosts(new HashSet<>());
            hero1.setComments(new HashSet<>());
            hero1.setGroups(new HashSet<>());
            hero1.setCreatedGroups(new HashSet<>());

            User user1 = new User();
//            user1.setId(UUID.randomUUID());
            user1.setUsername("admin");
            user1.setPassword(passwordEncoder.encode("admin1"));
            user1.setEmail("admin@find_a_dice.com");
            user1.setFirstName("John");
            user1.setLastName("Doe");
            user1.setDescription("Admin");
            user1.setRole(UserRoles.ADMIN);
            user1.setImageUrl("https://cdn.cloudflare.steamstatic.com/steamcommunity/public/images/avatars/77/777eb0797f3eb82da4255acb97932600ebbcb879_full.jpg");
            user1.setCreatedAt(LocalDateTime.now());

            user1.setHero(hero1);
            hero1.setUser(user1);

            userRepo.save(user1);
        }

    }
}
