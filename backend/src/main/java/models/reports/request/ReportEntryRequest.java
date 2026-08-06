package models.reports.request;

public class ReportEntryRequest {
    public String reportName;

    /** DEVRIYE, RADAR, KAZA, PERSONEL */
    public String reportType;

    public String cityId;

    public String unitId;

    public String startDate;

    public String endDate;

    public ReportEntryRequest() {
    }
}
