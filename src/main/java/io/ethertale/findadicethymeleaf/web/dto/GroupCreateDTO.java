package io.ethertale.findadicethymeleaf.web.dto;

import io.ethertale.findadicethymeleaf.hero.model.Hero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreateDTO {
    private String name;
    private String description;
    private String imageUrl;
}
