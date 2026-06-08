package io.ethertale.findadicethymeleaf.deletedReport.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeletedReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    @Enumerated(EnumType.STRING)
    private DeletedReportType type;

    @Column
    private UUID objectId;

    @Column(length = 1000)
    private String title;

    @Column(length = 10000)
    private String content;

    @Column
    private UUID deletedByUserId;

    @Column
    private LocalDateTime deletedOn;

    public String getFormatedTime(){
        return deletedOn.getDayOfMonth() +
                "/" +
                deletedOn.getMonthValue() +
                "/" +
                deletedOn.getYear() +
                "/" +
                deletedOn.getHour() +
                ":" +
                deletedOn.getMinute() +
                ":" +
                deletedOn.getSecond();
    }
}
