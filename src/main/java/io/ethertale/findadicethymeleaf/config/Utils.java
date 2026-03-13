package io.ethertale.findadicethymeleaf.config;

import java.time.LocalDateTime;

public class Utils {

    public String formattedTimeNow(){
        LocalDateTime now = LocalDateTime.now();
        return String.format("%02d:%02d | %d %s %d",
                now.getHour(), now.getMinute(),
                now.getDayOfMonth(), now.getMonth(), now.getYear());
    }
}
