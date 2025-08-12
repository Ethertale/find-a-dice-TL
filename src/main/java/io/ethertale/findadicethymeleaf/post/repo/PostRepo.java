package io.ethertale.findadicethymeleaf.post.repo;

import io.ethertale.findadicethymeleaf.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepo extends JpaRepository<Post, UUID> {
}
