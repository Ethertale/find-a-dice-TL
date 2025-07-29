package io.ethertale.findadicethymeleaf.user.service;

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
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setRole(UserRoles.USER);
        user.setCreatedAt(LocalDateTime.now());

        userRepo.save(user);
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

        log.info("At: {} User with ID: {} - and Username: {} logged in.", LocalDateTime.now(), user.getId(), username);

        return new AuthenticationDetails(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getDescription(), user.getRole(), user.getHero(), user.getImageUrl(), user.getCreatedAt());
    }
}
