package io.ethertale.findadicethymeleaf.user.repo;

import io.ethertale.findadicethymeleaf.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
}
