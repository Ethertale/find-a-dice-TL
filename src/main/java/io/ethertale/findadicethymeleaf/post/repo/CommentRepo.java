package io.ethertale.findadicethymeleaf.post.repo;

import io.ethertale.findadicethymeleaf.post.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepo extends JpaRepository<Comment, UUID> {
}
