package modules.reports.business;

import java.util.ArrayList;
import java.util.List;

import models.operations.entity.Task;
import models.personnel.entity.Police;
import models.regions.entity.City;
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
import modules.operations.business.TaskBusiness;
import modules.personnel.business.PoliceBusiness;
import modules.regions.business.RegionBusiness;
import modules.units.business.UnitBusiness;

public class ReportEntryBusiness {

    /** olusturulan raporlar */
    private static final List<ReportListItem> REPORTS = new ArrayList<>();

    private static final String[][] REPORT_TYPES = {
            { "DEVRIYE", "Devriye Raporu" },
            { "RADAR", "Radar Raporu" },
            { "KAZA", "Kaza Inceleme Raporu" },
            { "PERSONEL", "Personel Raporu" }
    };

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

    /** Rapor tipi secenekleri */
    public ReportTypeListResponse ReportTypeList(ReportTypeListRequest reportTypeListRequest) {

        ReportTypeListResponse reportTypeListResponse = new ReportTypeListResponse();

        for (String[] type : REPORT_TYPES) {
            reportTypeListResponse.types.add(new ReportTypeItem(type[0], type[1]));
        }

        return reportTypeListResponse;
    }

    /** Onay adimi: rapor olusturulmadan once kapsam ozeti doner */
    public ReportEntryConfirmResponse Confirm(ReportEntryRequest reportEntryRequest) {

        ReportEntryConfirmResponse response = new ReportEntryConfirmResponse();

        if (reportEntryRequest == null || reportEntryRequest.reportName == null
                || reportEntryRequest.reportName.trim().isEmpty()) {
            response.message = "Rapor adi girilmedi.";
            return response;
        }

        if (reportEntryRequest.reportType == null || reportEntryRequest.reportType.isEmpty()) {
            response.message = "Rapor tipi secilmedi.";
            return response;
        }

        response.valid = true;
        response.reportName = reportEntryRequest.reportName;
        response.reportTypeName = GetReportTypeName(reportEntryRequest.reportType);
        response.cityName = GetCityName(reportEntryRequest.cityId);
        response.unitName = GetUnitName(reportEntryRequest.unitId);
        response.period = reportEntryRequest.startDate + " - " + reportEntryRequest.endDate;
        response.policeCount = CountPolice(reportEntryRequest);
        response.taskCount = CountTasks(reportEntryRequest);
        response.message = "Rapor olusturulmaya hazir.";

        return response;
    }

    /** Gerceklestirme adimi: rapor olusturulur ve listeye eklenir */
    public ReportEntryExecuteResponse Execute(ReportEntryRequest reportEntryRequest) {

        ReportEntryExecuteResponse response = new ReportEntryExecuteResponse();

        if (reportEntryRequest == null || reportEntryRequest.reportName == null
                || reportEntryRequest.reportName.trim().isEmpty()) {
            response.message = "Rapor olusturulamadi.";
            return response;
        }

        ReportListItem report = new ReportListItem();
        report.reportNo = "RPR-" + (REPORTS.size() + 1001);
        report.reportName = reportEntryRequest.reportName;
        report.reportType = reportEntryRequest.reportType;
        report.reportTypeName = GetReportTypeName(reportEntryRequest.reportType);
        report.cityName = GetCityName(reportEntryRequest.cityId);
        report.unitName = GetUnitName(reportEntryRequest.unitId);
        report.period = reportEntryRequest.startDate + " - " + reportEntryRequest.endDate;
        report.policeCount = CountPolice(reportEntryRequest);
        report.taskCount = CountTasks(reportEntryRequest);
        report.createdDate = reportEntryRequest.endDate;

        REPORTS.add(report);

        response.success = true;
        response.reportNo = report.reportNo;
        response.reportName = report.reportName;
        response.reportTypeName = report.reportTypeName;
        response.cityName = report.cityName;
        response.period = report.period;
        response.policeCount = report.policeCount;
        response.taskCount = report.taskCount;
        response.createdDate = report.createdDate;
        response.message = "Rapor olusturuldu.";

        return response;
    }

    /** Olusturulmus raporlar */
    public ReportListResponse ReportList(ReportListRequest reportListRequest) {

        String reportType = reportListRequest == null || reportListRequest.reportType == null
                ? ""
                : reportListRequest.reportType;

        ReportListResponse reportListResponse = new ReportListResponse();

        for (ReportListItem report : REPORTS) {
            if (!reportType.isEmpty() && !reportType.equals(report.reportType)) {
                continue;
            }
            reportListResponse.reports.add(report);
        }

        reportListResponse.totalCount = reportListResponse.reports.size();

        return reportListResponse;
    }

    /* ---------------- yardimcilar ---------------- */

    private String GetCityName(String cityId) {
        if (cityId == null || cityId.isEmpty()) {
            return "Ulke geneli";
        }
        City city = RegionBusiness.GetCity(cityId);
        return city == null ? "" : city.name;
    }

    private String GetUnitName(String unitId) {
        if (unitId == null || unitId.isEmpty()) {
            return "Tum birimler";
        }
        for (Unit unit : UnitBusiness.GetUnits()) {
            if (unit.id.equals(unitId)) {
                return unit.name;
            }
        }
        return "";
    }

    private Integer CountPolice(ReportEntryRequest request) {
        String cityId = request.cityId == null ? "" : request.cityId;
        String unitId = request.unitId == null ? "" : request.unitId;

        int count = 0;
        for (Police police : PoliceBusiness.GetPoliceList()) {
            if (!cityId.isEmpty() && !police.cityId.equals(cityId)) {
                continue;
            }
            if (!unitId.isEmpty() && !police.unitId.equals(unitId)) {
                continue;
            }
            count++;
        }
        return count;
    }

    private Integer CountTasks(ReportEntryRequest request) {
        String cityId = request.cityId == null ? "" : request.cityId;
        String unitId = request.unitId == null ? "" : request.unitId;

        int count = 0;
        for (Task task : TaskBusiness.GetTasks()) {
            if (!cityId.isEmpty() && !task.cityId.equals(cityId)) {
                continue;
            }
            if (!unitId.isEmpty() && !task.unitId.equals(unitId)) {
                continue;
            }
            count++;
        }
        return count;
    }
}
