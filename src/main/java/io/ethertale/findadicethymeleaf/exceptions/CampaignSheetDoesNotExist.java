package io.ethertale.findadicethymeleaf.exceptions;

public class CampaignSheetDoesNotExist extends RuntimeException {
    public CampaignSheetDoesNotExist() {
    }

    public CampaignSheetDoesNotExist(String message) {
        super(message);
    }
}
