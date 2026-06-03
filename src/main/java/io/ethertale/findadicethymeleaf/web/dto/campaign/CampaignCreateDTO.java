package io.ethertale.findadicethymeleaf.web.dto.campaign;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignCreateDTO {
    private String title;
    private String description;
    private String imageUrl;
    private int maxPlayers;
}
