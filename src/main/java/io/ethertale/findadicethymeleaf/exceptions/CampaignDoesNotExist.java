package io.ethertale.findadicethymeleaf.exceptions;

public class CampaignDoesNotExist extends RuntimeException {
    public CampaignDoesNotExist() {
    }

    public CampaignDoesNotExist(String message) {
        super(message);
    }
}
