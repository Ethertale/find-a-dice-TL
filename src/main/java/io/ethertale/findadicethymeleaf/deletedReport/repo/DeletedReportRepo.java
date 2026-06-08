package io.ethertale.findadicethymeleaf.deletedReport.repo;

import io.ethertale.findadicethymeleaf.deletedReport.model.DeletedReport;
import io.ethertale.findadicethymeleaf.deletedReport.model.DeletedReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeletedReportRepo extends JpaRepository<DeletedReport, UUID> {
    List<DeletedReport> getAllByType(DeletedReportType type);
}
