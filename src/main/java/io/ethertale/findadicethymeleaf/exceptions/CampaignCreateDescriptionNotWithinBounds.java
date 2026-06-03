package io.ethertale.findadicethymeleaf.exceptions;

public class CampaignCreateDescriptionNotWithinBounds extends RuntimeException {
    public CampaignCreateDescriptionNotWithinBounds() {
    }

    public CampaignCreateDescriptionNotWithinBounds(String message) {
        super(message);
    }
}
