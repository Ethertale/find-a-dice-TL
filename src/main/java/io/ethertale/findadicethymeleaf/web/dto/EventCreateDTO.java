package io.ethertale.findadicethymeleaf.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateDTO {

    private String title;
    private String description;
    private String location;
    private String image;
    private LocalDate startTime;
}
