package io.ethertale.findadicethymeleaf.exceptions;

public class CampaignMembershipDoesNotExist extends RuntimeException {
    public CampaignMembershipDoesNotExist() {
    }

    public CampaignMembershipDoesNotExist(String message) {
        super(message);
    }
}
