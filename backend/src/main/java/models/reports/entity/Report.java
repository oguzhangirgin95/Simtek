package models.reports.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @Column(name = "report_no", length = 20)
    public String reportNo;

    @Column(name = "report_name", length = 150, nullable = false)
    public String reportName;

    @Column(name = "report_type", length = 20, nullable = false)
    public String reportType;

    @Column(name = "city_id", length = 10)
    public String cityId;

    @Column(name = "unit_id", length = 20)
    public String unitId;

    @Column(name = "city_name", length = 50)
    public String cityName;

    @Column(name = "unit_name", length = 100)
    public String unitName;

    @Column(name = "period", length = 50)
    public String period;

    @Column(name = "police_count")
    public Integer policeCount;

    @Column(name = "task_count")
    public Integer taskCount;

    @Column(name = "created_date")
    public LocalDate createdDate;

    public Report() {
    }
}
