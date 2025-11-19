package io.ethertale.findadicethymeleaf.web.dto;

import io.ethertale.findadicethymeleaf.group.model.Group;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupPostDTO {
    private String title;
    private Hero hero;
    private Group group;
    private String description;
    private String image;
}
