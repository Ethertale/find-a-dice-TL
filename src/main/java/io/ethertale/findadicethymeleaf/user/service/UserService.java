package io.ethertale.findadicethymeleaf.user.service;

import io.ethertale.findadicethymeleaf.hero.service.HeroService;
import io.ethertale.findadicethymeleaf.security.AuthenticationDetails;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.user.model.UserRoles;
import io.ethertale.findadicethymeleaf.user.repo.UserRepo;
import io.ethertale.findadicethymeleaf.web.dto.RegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final HeroService heroService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, HeroService heroService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.heroService = heroService;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterDTO registerDTO) {
        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .email(registerDTO.getEmail())
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .imageUrl("https://i.ibb.co/WWDv4mYx/Logo-Transparent.png")
                .role(UserRoles.USER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepo.save(user);

        User userByUsername = userRepo.getUserByUsername(user.getUsername());
        userByUsername.setHero(heroService.createFirstHero(userByUsername));

        userRepo.save(userByUsername);

    }

    public User getUserById(UUID id) {
        if (userRepo.findById(id).isEmpty()) {
            throw new UsernameNotFoundException(id.toString());
        }
        return userRepo.findById(id).get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        log.info("User {} logged in at {}:{} on {} {} {} - {}", username, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getMonth(), LocalDateTime.now().getYear(), user.getId());

        return new AuthenticationDetails(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getDescription(), user.getRole(), user.getHero(), user.getImageUrl(), user.getCreatedAt());
    }

    public List<User> getAllUsers() {
        return userRepo.findAll().stream().toList();
    }
}
