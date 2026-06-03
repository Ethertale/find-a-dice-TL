package io.ethertale.findadicethymeleaf.exceptions;

public class CampaignCreateTitleNotWithinBounds extends RuntimeException {
    public CampaignCreateTitleNotWithinBounds() {
    }

    public CampaignCreateTitleNotWithinBounds(String message) {
        super(message);
    }
}
