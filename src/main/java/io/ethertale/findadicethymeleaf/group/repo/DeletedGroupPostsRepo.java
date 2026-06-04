package io.ethertale.findadicethymeleaf.group.repo;

import io.ethertale.findadicethymeleaf.group.model.Group;
import io.ethertale.findadicethymeleaf.post.model.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeletedGroupPostsRepo extends JpaRepository<GroupPost, UUID> {
//    List<GroupPost> getDeletedGroupPosts();
//    Optional<GroupPost> getGroupPostById(UUID id);
}
