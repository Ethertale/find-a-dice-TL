package io.ethertale.findadicethymeleaf.exceptions;

import java.util.UUID;

public class GroupPostTooLongOrTooShort extends RuntimeException {
    public GroupPostTooLongOrTooShort(String message) {
        super(message);
    }

  public GroupPostTooLongOrTooShort() {
  }
}
