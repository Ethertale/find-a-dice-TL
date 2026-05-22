package io.ethertale.findadicethymeleaf.web.dto.campaign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WSCampaignOutgoingMessageDTO {
    private String senderHeroId;
    private String senderHeroName;
    private String senderHeroImageUrl;
    private String message;
    private String sentAt;
}