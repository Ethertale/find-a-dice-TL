package io.ethertale.findadicethymeleaf.web.dto.campaign;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterSheetDTO {

    private String name;
    private String age;
    private String gender;
    private String race;
    private String charClass;
    private String level;
    private String background;
    private String alignment;
    private String imageUrl;

    private String hp;
    private String maxHp;
    private String resourceName;
    private String currentResource;
    private String maxResource;

    private String strength;
    private String dexterity;
    private String constitution;
    private String intelligence;
    private String wisdom;
    private String charisma;

    private String backstory;
    private String inventory;
    private String spells;
    private String notes;
}