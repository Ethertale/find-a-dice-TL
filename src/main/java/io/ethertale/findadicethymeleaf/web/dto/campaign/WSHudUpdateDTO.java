package io.ethertale.findadicethymeleaf.web.dto.campaign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WSHudUpdateDTO {
    private String heroId;
    private String heroName;
    private String heroImageUrl;
    private String hp;
    private String maxHp;
    private String resourceName;
    private String currentResource;
    private String maxResource;
}
