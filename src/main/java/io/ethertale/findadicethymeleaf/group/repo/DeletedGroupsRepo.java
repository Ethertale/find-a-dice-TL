package io.ethertale.findadicethymeleaf.group.repo;

import io.ethertale.findadicethymeleaf.group.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeletedGroupsRepo extends JpaRepository<Group, UUID> {
//    List<Group> getDeletedGroups();
//    Optional<Group> getGroupById(UUID id);
}
