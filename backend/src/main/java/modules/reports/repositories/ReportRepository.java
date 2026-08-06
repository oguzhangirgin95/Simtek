package modules.reports.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import models.reports.entity.Report;

public interface ReportRepository extends JpaRepository<Report, String> {

    List<Report> findAllByOrderByReportNoAsc();

    List<Report> findByReportTypeOrderByReportNoAsc(String reportType);
}
