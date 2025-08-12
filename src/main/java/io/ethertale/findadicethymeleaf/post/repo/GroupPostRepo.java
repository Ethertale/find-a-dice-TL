package io.ethertale.findadicethymeleaf.post.repo;

import io.ethertale.findadicethymeleaf.post.model.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupPostRepo extends JpaRepository<GroupPost, UUID> {
}
