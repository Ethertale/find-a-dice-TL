package io.ethertale.findadicethymeleaf.group.repo;

import io.ethertale.findadicethymeleaf.group.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupRepo extends JpaRepository<Group, UUID> {
}
