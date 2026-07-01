package io.ethertale.findadicethymeleaf.user.service;

import io.ethertale.findadicethymeleaf.exceptions.RegisterEmailNotValid;
import io.ethertale.findadicethymeleaf.exceptions.RegisterPasswordNotInCharRange;
import io.ethertale.findadicethymeleaf.exceptions.RegisterUsernameNotInCharRange;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    @Transactional
    public void registerUser(RegisterDTO registerDTO) {
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@(gmail\\.com|abv\\.bg|yahoo\\.com|outlook\\.com|hotmail\\.com|live\\.com|icloud\\.com|proton\\.me|protonmail\\.com|aol\\.com|msn\\.com|gmx\\.com|mail\\.com)$");
        Matcher matcher = emailPattern.matcher(registerDTO.getEmail());

        if (registerDTO.getUsername().length() < 5 || registerDTO.getUsername().length() > 16) {
            throw new RegisterUsernameNotInCharRange();
        }
        if (registerDTO.getPassword().length() < 8 || registerDTO.getPassword().length() > 32) {
            throw new RegisterPasswordNotInCharRange();
        }

        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .imageUrl("https://api.dicebear.com/10.x/shapes/svg?seed=1")
                .role(UserRoles.USER)
                .createdAt(LocalDateTime.now())
                .build();

        if (!matcher.find()) {
            throw new RegisterEmailNotValid();
        }else {
            user.setEmail(matcher.group());
        }

        userRepo.save(user);

        User userByUsername = userRepo.getUserByUsername(user.getUsername());
        userByUsername.setHero(heroService.createFirstHero(userByUsername));
        userByUsername.setImageUrl("https://api.dicebear.com/10.x/shapes/svg?seed=" + userByUsername.getId());

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

    public List<User> searchUsers(String query) {
        return userRepo.findUserByUsernameContainingIgnoreCase(query);
    }
}
