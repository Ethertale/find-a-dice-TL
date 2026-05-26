package io.ethertale.findadicethymeleaf.exceptions;

public class CampaignCreateInvalidMaxPlayers extends RuntimeException {
    public CampaignCreateInvalidMaxPlayers() {
    }

    public CampaignCreateInvalidMaxPlayers(String message) {
        super(message);
    }
}
