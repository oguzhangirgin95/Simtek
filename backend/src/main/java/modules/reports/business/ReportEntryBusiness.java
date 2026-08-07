package modules.reports.business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import models.operations.entity.Task;
import models.personnel.entity.Police;
import models.regions.entity.City;
import models.reports.entity.Report;
import models.reports.request.ReportEntryRequest;
import models.reports.request.ReportListRequest;
import models.reports.request.ReportTypeListRequest;
import models.reports.response.ReportEntryConfirmResponse;
import models.reports.response.ReportEntryExecuteResponse;
import models.reports.response.ReportListItem;
import models.reports.response.ReportListResponse;
import models.reports.response.ReportTypeItem;
import models.reports.response.ReportTypeListResponse;
import models.units.entity.Unit;
import modules.operations.repositories.TaskRepository;
import modules.personnel.repositories.PoliceRepository;
import modules.regions.repositories.CityRepository;
import modules.reports.repositories.ReportRepository;
import modules.units.repositories.UnitRepository;

@Service
public class ReportEntryBusiness {

    private static final String[][] REPORT_TYPES = {
            { "DEVRIYE", "Devriye Raporu" },
            { "RADAR", "Radar Raporu" },
            { "KAZA", "Kaza Inceleme Raporu" },
            { "PERSONEL", "Personel Raporu" }
    };

    private final ReportRepository reportRepository;
    private final PoliceRepository policeRepository;
    private final TaskRepository taskRepository;
    private final CityRepository cityRepository;
    private final UnitRepository unitRepository;

    public ReportEntryBusiness(ReportRepository reportRepository, PoliceRepository policeRepository,
            TaskRepository taskRepository, CityRepository cityRepository, UnitRepository unitRepository) {
        this.reportRepository = reportRepository;
        this.policeRepository = policeRepository;
        this.taskRepository = taskRepository;
        this.cityRepository = cityRepository;
        this.unitRepository = unitRepository;
    }

    public static String GetReportTypeName(String reportType) {
        if (reportType == null) {
            return "";
        }
        for (String[] type : REPORT_TYPES) {
            if (type[0].equals(reportType)) {
                return type[1];
            }
        }
        return reportType;
    }

    public ReportTypeListResponse ReportTypeList(ReportTypeListRequest reportTypeListRequest) {

        ReportTypeListResponse response = new ReportTypeListResponse();

        for (String[] type : REPORT_TYPES) {
            response.types.add(new ReportTypeItem(type[0], type[1]));
        }

        return response;
    }

    @Transactional(readOnly = true)
    public ReportEntryConfirmResponse Confirm(ReportEntryRequest request) {

        ReportEntryConfirmResponse response = new ReportEntryConfirmResponse();

        if (request == null || !HasText(request.reportName)) {
            response.message = "Rapor adi girilmedi.";
            return response;
        }
        if (!HasText(request.reportType)) {
            response.message = "Rapor tipi secilmedi.";
            return response;
        }

        response.valid = true;
        response.reportName = request.reportName;
        response.reportTypeName = GetReportTypeName(request.reportType);
        response.cityName = GetCityName(request.cityId);
        response.unitName = GetUnitName(request.unitId);
        response.period = request.startDate + " - " + request.endDate;
        response.policeCount = CountPolice(request);
        response.taskCount = CountTasks(request);
        response.message = "Rapor olusturulmaya hazir.";

        return response;
    }

    @Transactional
    public ReportEntryExecuteResponse Execute(ReportEntryRequest request) {

        ReportEntryExecuteResponse response = new ReportEntryExecuteResponse();

        if (request == null || !HasText(request.reportName)) {
            response.message = "Rapor olusturulamadi.";
            return response;
        }

        Report report = new Report();
        report.reportNo = "RPR-" + (reportRepository.count() + 1001);
        report.reportName = request.reportName.trim();
        report.reportType = request.reportType;
        report.cityId = request.cityId;
        report.unitId = request.unitId;
        report.cityName = GetCityName(request.cityId);
        report.unitName = GetUnitName(request.unitId);
        report.period = request.startDate + " - " + request.endDate;
        report.policeCount = CountPolice(request);
        report.taskCount = CountTasks(request);
        report.createdDate = HasText(request.endDate) ? LocalDate.parse(request.endDate) : LocalDate.now();

        reportRepository.save(report);

        response.success = true;
        response.reportNo = report.reportNo;
        response.reportName = report.reportName;
        response.reportTypeName = GetReportTypeName(report.reportType);
        response.cityName = report.cityName;
        response.period = report.period;
        response.policeCount = report.policeCount;
        response.taskCount = report.taskCount;
        response.createdDate = report.createdDate.toString();
        response.message = "Rapor olusturuldu.";

        return response;
    }

    @Transactional(readOnly = true)
    public ReportListResponse ReportList(ReportListRequest reportListRequest) {

        String reportType = reportListRequest == null ? null : reportListRequest.reportType;

        List<Report> reports = HasText(reportType)
                ? reportRepository.findByReportTypeOrderByReportNoAsc(reportType)
                : reportRepository.findAllByOrderByReportNoAsc();

        ReportListResponse response = new ReportListResponse();

        for (Report report : reports) {
            ReportListItem item = new ReportListItem();
            item.reportNo = report.reportNo;
            item.reportName = report.reportName;
            item.reportType = report.reportType;
            item.reportTypeName = GetReportTypeName(report.reportType);
            item.cityName = report.cityName;
            item.unitName = report.unitName;
            item.period = report.period;
            item.policeCount = report.policeCount;
            item.taskCount = report.taskCount;
            item.createdDate = report.createdDate == null ? null : report.createdDate.toString();

            response.reports.add(item);
        }

        response.totalCount = response.reports.size();

        return response;
    }


    private String GetCityName(String cityId) {
        if (!HasText(cityId)) {
            return "Ulke geneli";
        }
        City city = cityRepository.findById(cityId).orElse(null);
        return city == null ? "" : city.name;
    }

    private String GetUnitName(String unitId) {
        if (!HasText(unitId)) {
            return "Tum birimler";
        }
        Unit unit = unitRepository.findById(unitId).orElse(null);
        return unit == null ? "" : unit.name;
    }

    private Integer CountPolice(ReportEntryRequest request) {
        Specification<Police> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (HasText(request.cityId)) {
                predicates.add(builder.equal(root.get("cityId"), request.cityId));
            }
            if (HasText(request.unitId)) {
                predicates.add(builder.equal(root.get("unitId"), request.unitId));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
        return (int) policeRepository.count(specification);
    }

    private Integer CountTasks(ReportEntryRequest request) {
        Specification<Task> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (HasText(request.cityId)) {
                predicates.add(builder.equal(root.get("cityId"), request.cityId));
            }
            if (HasText(request.unitId)) {
                predicates.add(builder.equal(root.get("unitId"), request.unitId));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
        return (int) taskRepository.count(specification);
    }

    private static boolean HasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
